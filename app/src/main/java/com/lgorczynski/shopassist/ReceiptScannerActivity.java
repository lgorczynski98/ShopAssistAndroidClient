package com.lgorczynski.shopassist;

import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class ReceiptScannerActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static final String TAG = "ReceiptScannerActivity";
    private JavaCameraView javaCameraView;
    private Mat mRGBA;
    private Mat imgGray;
    private Mat imgCanny;
    private BaseLoaderCallback mLoader = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            if(status != LoaderCallbackInterface.SUCCESS)
                super.onManagerConnected(status);
            else{
                javaCameraView.enableView();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_scanner);
        javaCameraView = findViewById(R.id.java_camera_view);
        javaCameraView.setVisibility(SurfaceView.VISIBLE);
        javaCameraView.setCvCameraViewListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(javaCameraView != null)
            javaCameraView.disableView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(javaCameraView != null)
            javaCameraView.disableView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!OpenCVLoader.initDebug()){
            Log.d(TAG, "onResume: OpenCV problems while loading");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, this, mLoader);
        }
        else{
            Log.d(TAG, "onResume: Succes on loading OpenCV");
            mLoader.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    public void onCameraViewStopped() {
        mRGBA.release();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRGBA = new Mat(height, width, CvType.CV_8UC4);
        imgGray = new Mat(height, width, CvType.CV_8UC1);
        imgCanny = new Mat(height, width, CvType.CV_8UC1);
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRGBA = inputFrame.rgba();
        Imgproc.cvtColor(mRGBA, imgGray, Imgproc.COLOR_RGBA2GRAY);
        Imgproc.Canny(imgGray, imgCanny, 50, 100);
        return imgCanny;
    }
}
