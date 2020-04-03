package com.lgorczynski.shopassist.ui.receipt_scanner;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.lgorczynski.shopassist.R;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class ReceiptScannerCameraFragment extends Fragment implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static final String TAG = "ReceiptScannerCameraFra";
    private NavController navController;

    private ImageButton captureButton;
    private JavaCameraView javaCameraView;
    private Mat mRGBA;
    private Mat imgGray;
    private Mat imgCanny;
    private BaseLoaderCallback mLoader = new BaseLoaderCallback(getContext()) {
        @Override
        public void onManagerConnected(int status) {
            if(status != LoaderCallbackInterface.SUCCESS)
                super.onManagerConnected(status);
            else{
                javaCameraView.enableView();
            }
        }
    };
    private static Mat morph_kernel = new Mat(new Size(10, 10), CvType.CV_8UC1, new Scalar(255));
    private static final int SCALE_FACTOR = 4;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_receipt_scanner_camera, container, false);
        captureButton = root.findViewById(R.id.receipt_scanner_capture_button);
        captureButton.setOnClickListener(view -> navController.navigate(R.id.action_receiptScannerCameraFragment_to_receiptScannerImagePreviewFragment));

        javaCameraView = root.findViewById(R.id.java_camera_view);
        javaCameraView.setVisibility(SurfaceView.VISIBLE);
        javaCameraView.setCvCameraViewListener(this);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(javaCameraView != null)
            javaCameraView.disableView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(javaCameraView != null)
            javaCameraView.disableView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!OpenCVLoader.initDebug()){
            Log.d(TAG, "onResume: OpenCV problems while loading");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, getContext(), mLoader);
        }
        else{
            Log.d(TAG, "onResume: Succes on loading OpenCV");
            mLoader.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    public void onCameraViewStopped() {
        mRGBA.release();
        imgGray.release();
        imgCanny.release();
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
        try {
            Mat mResizedRGBA = new Mat();
            Imgproc.resize(mRGBA, mResizedRGBA, new Size(mRGBA.cols()/ SCALE_FACTOR, mRGBA.rows()/ SCALE_FACTOR));

            List<MatOfPoint> matOfPoints = new ArrayList<>();
            List<Point> points = getRect(mResizedRGBA).toList();
            points.forEach(point -> {
                point.x = point.x*SCALE_FACTOR;
                point.y = point.y*SCALE_FACTOR;
            });
            MatOfPoint matOfPoint = new MatOfPoint();
            matOfPoint.fromList(points);
            matOfPoints.add(matOfPoint);
            Imgproc.drawContours(mRGBA, matOfPoints, -1, new Scalar(0, 255, 0, 128), 4);
        }
        catch(Exception e) {
            Log.d(TAG, "onCameraFrame: Strange exception on drawing contours");
        }
//        Mat mRGBAT = mRGBA.t();
//        Core.flip(mRGBAT, mRGBAT, 1);
//        Imgproc.resize(mRGBAT, mRGBAT, mRGBA.size());
//        return mRGBAT;
        return mRGBA;

//        Imgproc.cvtColor(mRGBA, imgGray, Imgproc.COLOR_RGBA2GRAY);
//        Imgproc.medianBlur(imgGray, imgCanny, 9);
//        Imgproc.Canny(imgGray, imgCanny, 100, 300);
//        List<MatOfPoint> contours = new ArrayList<>();
//        //new Mat to hierarchy
//        Imgproc.findContours(imgCanny, contours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
//        if(contours.size() != 0){
//            try {
//                List<MatOfPoint> matOfPoints = new ArrayList<>();
//                matOfPoints.add(getLargestContour(contours));
//                Imgproc.drawContours(mRGBA, matOfPoints, 0, new Scalar(255, 0, 0), -1);
//            }
//            catch(Exception e) {
//                Log.d(TAG, "onCameraFrame: nie wiadomo jaki blad");
//            }
//        }
//        return mRGBA;
    }

    private MatOfPoint getLargestContour(List<MatOfPoint> contours){
        MatOfPoint largest = new MatOfPoint();
        double maxArea = 0;
        for (MatOfPoint contour : contours) {
            double area = Imgproc.contourArea(contour);
            if (area > 1000 ){
                MatOfPoint2f contour2f = new MatOfPoint2f();
                contour.convertTo(contour2f, CvType.CV_32F);
                double approxDistance = Imgproc.arcLength(contour2f, true) * 0.02;
                MatOfPoint2f approxCurve = new MatOfPoint2f();
                Imgproc.approxPolyDP(contour2f, approxCurve, approxDistance,true);
                MatOfPoint points = new MatOfPoint(approxCurve.toArray());
                if(area > maxArea && points.total() == 4){
                    largest = points;
                    maxArea = area;
                }
            }
        }
        return largest;
    }

    private MatOfPoint getRect(Mat img){
        Imgproc.cvtColor(img, imgGray, Imgproc.COLOR_BGR2GRAY, 4);
        Imgproc.blur(imgGray, imgGray, new Size(3, 3));
        Core.normalize(imgGray, imgGray, 0, 255, Core.NORM_MINMAX);
        Imgproc.threshold(imgGray,imgGray, 150,255,Imgproc.THRESH_TRUNC);
        Core.normalize(imgGray, imgGray, 0, 255, Core.NORM_MINMAX);
        Imgproc.Canny(imgGray, imgCanny, 185, 85);
        Imgproc.threshold(imgCanny,imgCanny,155,255,Imgproc.THRESH_TOZERO);
        Imgproc.morphologyEx(imgCanny, imgCanny, Imgproc.MORPH_CLOSE, morph_kernel, new Point(-1,-1),1);
        List<MatOfPoint> contours = new ArrayList<>();
//        //new Mat to hierarchy
        Imgproc.findContours(imgCanny, contours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        return getLargestContour(contours);

    }
}
