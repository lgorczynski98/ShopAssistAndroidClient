package com.lgorczynski.shopassist.ui.receipt_scanner;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.lgorczynski.shopassist.R;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import io.fotoapparat.Fotoapparat;
import io.fotoapparat.configuration.UpdateConfiguration;
import io.fotoapparat.parameter.ScaleType;
import io.fotoapparat.preview.Frame;
import io.fotoapparat.preview.FrameProcessor;
import io.fotoapparat.result.BitmapPhoto;
import io.fotoapparat.result.PhotoResult;
import io.fotoapparat.result.WhenDoneListener;
import io.fotoapparat.view.CameraView;
import io.fotoapparat.view.FocusView;

import static android.graphics.ImageFormat.NV21;
import static io.fotoapparat.selector.AspectRatioSelectorsKt.standardRatio;
import static io.fotoapparat.selector.FlashSelectorsKt.off;
import static io.fotoapparat.selector.FlashSelectorsKt.torch;
import static io.fotoapparat.selector.PreviewFpsRangeSelectorsKt.highestFps;
import static io.fotoapparat.selector.ResolutionSelectorsKt.highestResolution;
import static io.fotoapparat.selector.SensorSensitivitySelectorsKt.highestSensorSensitivity;

public class ReceiptScannerFotoapparat extends Fragment {

    private static final String TAG = "ReceiptScannerFotoappar";
    private NavController navController;

    private CameraView cameraView;
    private FocusView focusView;
    private View capture;
    private Fotoapparat fotoapparat;
    private SwitchCompat torchSwitch;
    private SwitchCompat autoDetectSwitch;
    private boolean autoDetectOn;
    private SurfaceView surfaceView;
    private MyFrameProcessor frameProcessor;
    private BaseLoaderCallback mLoader = new BaseLoaderCallback(getContext()) {
        @Override
        public void onManagerConnected(int status) {
            if(status != LoaderCallbackInterface.SUCCESS)
                super.onManagerConnected(status);
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_receipt_scanner_camera, container, false);

        cameraView = root.findViewById(R.id.cameraView);
        focusView = root.findViewById(R.id.focusView);
        capture = root.findViewById(R.id.capture);
        torchSwitch = root.findViewById(R.id.torchSwitch);
        autoDetectSwitch = root.findViewById(R.id.autoDetectSwitch);
        autoDetectOn = true;

        surfaceView = root.findViewById(R.id.surfaceView);
        surfaceView.setZOrderOnTop(true);
        SurfaceHolder sfhTrackHolder = surfaceView.getHolder();
        sfhTrackHolder.setFormat(PixelFormat.TRANSPARENT);

        frameProcessor = new MyFrameProcessor(cameraView.getHeight(), cameraView.getWidth());
        fotoapparat = createFotoapparat();

        takePictureOnClick();
        toggleTorchOnSwitch();
        toogleAutoDetectSwitch();

        cameraView.setVisibility(View.VISIBLE);

        return root;
    }

    private Fotoapparat createFotoapparat() {
        return Fotoapparat
                .with(getContext())
                .into(cameraView)
                .focusView(focusView)
                .previewFpsRange(highestFps())
                .sensorSensitivity(highestSensorSensitivity())
                .photoResolution(standardRatio(highestResolution()))
                .previewScaleType(ScaleType.CenterCrop)
                .frameProcessor(frameProcessor)
                .build();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    @Override
    public void onStart() {
        super.onStart();
        fotoapparat.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        fotoapparat.stop();
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

    private void toggleTorchOnSwitch() {
        torchSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                fotoapparat.updateConfiguration(
                        UpdateConfiguration.builder()
                                .flash(
                                        isChecked ? torch() : off()
                                )
                                .build()
                );
            }
        });
    }

    private void toogleAutoDetectSwitch(){
        autoDetectSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                autoDetectOn = !autoDetectOn;
                try {
                    Canvas canvas = surfaceView.getHolder().lockCanvas();
                    canvas.drawColor(0, PorterDuff.Mode.CLEAR);
                    surfaceView.getHolder().unlockCanvasAndPost(canvas);
                }
                catch(Exception e) {
                    Log.d(TAG, "onCheckedChanged: Error when canvas is null on leaving fragment");
                }
            }
        });
    }

    private void takePictureOnClick() {
        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
    }

    private void takePicture() {
        PhotoResult photoResult = fotoapparat.takePicture();

        photoResult
                .toBitmap()
                .whenDone(new WhenDoneListener<BitmapPhoto>() {
                    @Override
                    public void whenDone(@Nullable BitmapPhoto bitmapPhoto) {
                        if (bitmapPhoto == null) {
                            Log.e(TAG, "Couldn't capture photo.");
                            return;
                        }

                        Mat mat = new Mat();
                        Bitmap bmp32 = bitmapPhoto.bitmap.copy(Bitmap.Config.ARGB_8888, true);
                        Utils.bitmapToMat(bmp32, mat);
                        if(autoDetectOn){
                            List<Point> points = frameProcessor.getRect(mat).toList();
                            if(points.size() != 4){
                                navigateToImageCrop(bmp32);
                                return;
                            }

                            Bitmap croppedBitmap = frameProcessor.cropToPoints(points, mat);
                            mat.release();

                            Bundle bundle = new Bundle();
                            bundle.putParcelable("bitmap", croppedBitmap);
                            navController.navigate(R.id.action_receiptScannerCameraFragment_to_receiptScannerImagePreviewFragment, bundle);
                        }
                        else
                            navigateToImageCrop(bmp32);
                    }
                });
    }

    private void navigateToImageCrop(Bitmap bmp32){
        Bundle bundle = new Bundle();
        bundle.putParcelable("scannedPreview", bmp32);
        navController.navigate(R.id.action_receiptScannerCameraFragment_to_receiptScannerImageCropFragment, bundle);
    }

    private class MyFrameProcessor implements FrameProcessor {

        private List<Point> points;
        private Mat morph_kernel = new Mat(new Size(10, 10), CvType.CV_8UC1, new Scalar(255));
        private float SCREEN_WIDTH_SCALE;
        private float SCREEN_HEIGHT_SCALE;
        private float screenWidth;
        private float screenHeight;
        private Paint paint;
        private Mat mRGBA;
        private Canvas canvas;

        MyFrameProcessor(int height, int width){
            mRGBA = new Mat(height, width, CvType.CV_8UC4);
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.RED);
            paint.setStyle(Paint.Style.FILL);
            DisplayMetrics metrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
            screenHeight = metrics.heightPixels;
            screenWidth = metrics.widthPixels;
        }

        @Override
        public void process(@NonNull Frame frame) {
            if(!autoDetectOn)
                return;
            int frameWidth = frame.getSize().width;
            int frameHeight = frame.getSize().height;
            SCREEN_HEIGHT_SCALE = screenHeight / (float)frameHeight;
            SCREEN_WIDTH_SCALE = screenWidth / (float)frameWidth;
            YuvImage yuvImage = new YuvImage(frame.getImage(), NV21, frameWidth, frameHeight, null);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            yuvImage.compressToJpeg(new Rect(0, 0, frameWidth, frameHeight), 100, os);
            byte[] jpegByteArray = os.toByteArray();
            Bitmap bitmap = BitmapFactory.decodeByteArray(jpegByteArray, 0, jpegByteArray.length);
            Bitmap bmp32 = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            Utils.bitmapToMat(bmp32, mRGBA);
            try {
                points = getRect(mRGBA).toList();
                correctPoints();

                if(!autoDetectOn)
                    return;
                canvas = surfaceView.getHolder().lockCanvas();
                canvas.drawColor(0, PorterDuff.Mode.CLEAR);
                points.forEach(point -> canvas.drawCircle((float)point.x, (float)point.y, 30, paint));
                surfaceView.getHolder().unlockCanvasAndPost(canvas);
            }
            catch(Exception e) {
                Log.d(TAG, "onCameraFrame: Strange exception on drawing contours");
                e.printStackTrace();
            }
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

        private void correctPoints(){
            List<Point> correctedPoints = new ArrayList<>();
            points.forEach(point -> {
                float oldX = (float)point.x * SCREEN_WIDTH_SCALE;
                float oldY = (float)point.y * SCREEN_HEIGHT_SCALE;
                float newX = ((screenHeight - oldY) / screenHeight) * screenWidth;
                float newY = (oldX / screenWidth) * screenHeight;
                correctedPoints.add(new Point(newX, newY));
            });
            points = correctedPoints;
        }

        private MatOfPoint getRect(Mat mRGBA){
            Mat img = new Mat();
            mRGBA.copyTo(img);
            Bitmap bitmap = Bitmap.createBitmap(img.cols(), img.rows(), Bitmap.Config.ARGB_8888);
            Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2GRAY, 4);
            Imgproc.blur(img, img, new Size(7, 7));
            Utils.matToBitmap(img, bitmap);
            Core.normalize(img, img, 0, 255, Core.NORM_MINMAX);
            Utils.matToBitmap(img, bitmap);
            Imgproc.threshold(img,img, 150,255,Imgproc.THRESH_TRUNC);
            Utils.matToBitmap(img, bitmap);
            Core.normalize(img, img, 0, 255, Core.NORM_MINMAX);
            Utils.matToBitmap(img, bitmap);
            Imgproc.Canny(img, img, 185, 85);
            Utils.matToBitmap(img, bitmap);
            Imgproc.threshold(img,img,155,255,Imgproc.THRESH_TOZERO);
            Utils.matToBitmap(img, bitmap);
            Imgproc.morphologyEx(img, img, Imgproc.MORPH_CLOSE, morph_kernel, new Point(-1,-1),1);
            Utils.matToBitmap(img, bitmap);
            List<MatOfPoint> contours = new ArrayList<>();
//        //new Mat to hierarchy
            Imgproc.findContours(img, contours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
            return getLargestContour(contours);
        }

        private MatOfPoint getPage(Mat mRGBA){
            Mat img = new Mat();
            mRGBA.copyTo(img);

            Bitmap bitmap = Bitmap.createBitmap(img.cols(), img.rows(), Bitmap.Config.ARGB_8888);
            Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2GRAY);
            Utils.matToBitmap(img, bitmap);
            Imgproc.blur(img, img, new Size(3, 3));
            Utils.matToBitmap(img, bitmap);
            Imgproc.Canny(img, img, 70, 200, 3);
            Utils.matToBitmap(img, bitmap);
            List<MatOfPoint> contours = new ArrayList<>();
//        //new Mat to hierarchy
            Imgproc.findContours(img, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
            return getLargestContour(contours);
        }

        private Bitmap cropToPoints(List<Point> corners, Mat mRGBA){

            if(corners.size() == 0)
                return null;
            sortCorners(corners);

            double top = Math.sqrt(Math.pow(corners.get(0).x -
                    corners.get(1).x, 2) + Math.pow(corners.get(0).y -
                    corners.get(1).y, 2));
            double right = Math.sqrt(Math.pow(corners.get(1).x -
                    corners.get(2).x, 2) + Math.pow(corners.get(1).y -
                    corners.get(2).y, 2));
            double bottom = Math.sqrt(Math.pow(corners.get(2).x -
                    corners.get(3).x, 2) + Math.pow(corners.get(2).y -
                    corners.get(3).y, 2));
            double left = Math.sqrt(Math.pow(corners.get(3).x -
                    corners.get(0).x, 2) + Math.pow(corners.get(3).y -
                    corners.get(0).y, 2));
            Mat quad = Mat.zeros(new Size(Math.max(top, bottom),
                    Math.max(left, right)), CvType.CV_8UC3);

            ArrayList<Point> result_pts = new ArrayList<>();
            result_pts.add(new Point(quad.cols(), 0));
            result_pts.add(new Point(0, 0));
            result_pts.add(new Point(0, quad.rows()));
            result_pts.add(new Point(quad.cols(), quad.rows()));

            Mat cornerPts = Converters.vector_Point2f_to_Mat(corners);
            Mat resultPts = Converters.vector_Point2f_to_Mat(result_pts);

            Mat transformation = Imgproc.getPerspectiveTransform(cornerPts, resultPts);
            Imgproc.warpPerspective(mRGBA, quad, transformation, quad.size());
            Bitmap bitmap = Bitmap.createBitmap(quad.cols(), quad.rows(),
                    Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(quad, bitmap);
            return bitmap;
        }

        private void sortCorners(List<Point> corners) {
            List<Point> top = new ArrayList<>();
            List<Point> bottom = new ArrayList<>();
            Point center = new Point();
            for(int i=0; i<corners.size(); i++){
//                center.x += corners.get(i).x/corners.size();
//                center.y += corners.get(i).y/corners.size();
                center.x += corners.get(i).x;
                center.y += corners.get(i).y;
            }
            center.x /= corners.size();
            center.y /= corners.size();

            for (int i = 0; i < corners.size(); i++)
            {
                if (corners.get(i).y < center.y)
                    top.add(corners.get(i));
                else
                    bottom.add(corners.get(i));
            }
            corners = new ArrayList<>();
            if (top.size() == 2 && bottom.size() == 2){
                Point top_left = top.get(0).x > top.get(1).x ?
                        top.get(1) : top.get(0);
                Point top_right = top.get(0).x > top.get(1).x ?
                        top.get(0) : top.get(1);
                Point bottom_left = bottom.get(0).x > bottom.get(1).x
                        ? bottom.get(1) : bottom.get(0);
                Point bottom_right = bottom.get(0).x > bottom.get(1).x
                        ? bottom.get(0) : bottom.get(1);

                corners.add(top_left);
                corners.add(top_right);
                corners.add(bottom_right);
                corners.add(bottom_left);
            }
        }

    }
}
