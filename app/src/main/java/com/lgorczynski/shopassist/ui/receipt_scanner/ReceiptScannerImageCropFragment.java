package com.lgorczynski.shopassist.ui.receipt_scanner;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.lgorczynski.shopassist.R;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;

import java.util.ArrayList;
import java.util.List;


public class ReceiptScannerImageCropFragment extends Fragment implements View.OnTouchListener, View.OnClickListener {

    private static final String TAG = "ReceiptScannerImageCrop";
    private NavController navController;

    private float dX;
    private float dY;

    private float screenHeight;
    private float screenWidth;

    private int imageViewOffsetX;
    private int imageViewOffsetY;
    private int imageViewActualWidth;
    private int imageViewActualHeight;

    private static List<Point> savedPoints;
    private List<ImageButton> corners;
    private Bitmap srcBitmap;
    private Bitmap rotatedBitmap;

    private ImageView pointPreview;
    private SurfaceView surfaceView;
    private Canvas canvas;
    private Paint primaryColorPaint;
    private Paint accentColorPaint;

    private float SCREEN_WIDTH_SCALE;
    private float SCREEN_HEIGHT_SCALE;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_receipt_scanner_crop, container, false);

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenHeight = metrics.heightPixels;
        screenWidth = metrics.widthPixels;

        pointPreview = root.findViewById(R.id.receipt_scanner_crop_point_preview);
        surfaceView = root.findViewById(R.id.receipt_scanner_crop_surface_view);
        surfaceView.setZOrderOnTop(true);
        SurfaceHolder sfhTrackHolder = surfaceView.getHolder();
        sfhTrackHolder.setFormat(PixelFormat.TRANSPARENT);

        preparePaints();

        final ImageView previewImage = root.findViewById(R.id.receipt_scanner_crop_image);
        srcBitmap = getArguments().getParcelable("scannedPreview");
        rotatedBitmap = rotateBitmap(srcBitmap, 90);
        previewImage.setImageBitmap(rotatedBitmap);

        previewImage.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                imageViewOffsetX = previewImage.getLeft();
                imageViewOffsetY = previewImage.getTop();
                imageViewActualWidth = previewImage.getWidth();
                imageViewActualHeight = previewImage.getHeight();

                SCREEN_HEIGHT_SCALE = (float)imageViewActualHeight / rotatedBitmap.getHeight();
                SCREEN_WIDTH_SCALE = (float)imageViewActualWidth / rotatedBitmap.getWidth();

                Log.d(TAG, "onGlobalLayout: offset x: " + imageViewOffsetX + ", offset y: " + imageViewOffsetY);
                Log.d(TAG, "onGlobalLayout: actual width: " + imageViewActualWidth + ", actual height: " + imageViewActualHeight);
                Log.d(TAG, "onGlobalLayout: width scale: " + SCREEN_WIDTH_SCALE + ", height scale: " + SCREEN_HEIGHT_SCALE);

//                if(savedPoints != null){
//                    for (int i = 0; i < 4; i++) {
//                        // TODO: 24.04.2020 Do poprawy - nie jest dokladnie
//                        corners.get(i).setX((float)savedPoints.get(i).x);
//                        corners.get(i).setY((float)savedPoints.get(i).y + imageViewOffsetY);
//                    }
//                }

                previewImage.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        final ImageButton applyButton = root.findViewById(R.id.receipt_scanner_crop_apply);
        applyButton.setOnClickListener(this);

        corners = getCornerClickables(root);
        setDefaultCornersPositions(corners);
//        drawLines();

        return root;
    }

    private void preparePaints(){
        primaryColorPaint = new Paint();
        primaryColorPaint.setAntiAlias(true);
        primaryColorPaint.setDither(true);
        primaryColorPaint.setColor(Color.rgb(87, 215, 238));
        primaryColorPaint.setStyle(Paint.Style.STROKE);
        primaryColorPaint.setPathEffect(new DashPathEffect(new float[]{20, 20}, 0));
        primaryColorPaint.setStrokeWidth(12);

        accentColorPaint = new Paint();
        accentColorPaint.setColor(Color.rgb(216, 27, 96));
    }

    private Bitmap rotateBitmap(Bitmap bitmap, int degrees){
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        return rotatedBitmap;
    }

    private List<ImageButton> getCornerClickables(View root){
        List<ImageButton> corners = new ArrayList<>();
        ImageButton imgButton1 = root.findViewById(R.id.imageButton);
        ImageButton imgButton2 = root.findViewById(R.id.imageButton2);
        ImageButton imgButton3 = root.findViewById(R.id.imageButton3);
        ImageButton imgButton4 = root.findViewById(R.id.imageButton4);
        imgButton1.setOnTouchListener(this);
        imgButton2.setOnTouchListener(this);
        imgButton3.setOnTouchListener(this);
        imgButton4.setOnTouchListener(this);
        corners.add(imgButton1);
        corners.add(imgButton2);
        corners.add(imgButton3);
        corners.add(imgButton4);
        return corners;
    }

//    private void placeCornersCorrectly(List<ImageButton> corners, List<Point> points){
//        if(points.size() == 4){
//            for (int i = 0; i < 4; i++) {
//                setCornerCentreX(corners.get(i), (float)points.get(i).x * SCREEN_WIDTH_SCALE);
//                setCornerCentreY(corners.get(i), (float)points.get(i).y * SCREEN_HEIGHT_SCALE);
//            }
////            correctCorners(corners);
//        }
//        else{
//            setDefaultCornersPositions(corners);
//        }
//    }

    private void setDefaultCornersPositions(List<ImageButton> corners){

        setCornerCentreX(corners.get(0), 100);
        setCornerCentreY(corners.get(0), 300);

        setCornerCentreX(corners.get(1), screenWidth - 200);
        setCornerCentreY(corners.get(1), 300);

        setCornerCentreX(corners.get(2), screenWidth - 200);
        setCornerCentreY(corners.get(2), screenHeight - 400);

        setCornerCentreX(corners.get(3), 100);
        setCornerCentreY(corners.get(3), screenHeight - 400);
    }

    private void setCornerCentreX(ImageButton corner, float centreX){
        corner.setX(centreX - (float)corner.getWidth()/2);
    }

    private void setCornerCentreY(ImageButton corner, float centreY){
        corner.setY(centreY - (float)corner.getHeight()/2);
    }

    private float getCornerCentreX(ImageButton corner){
        return corner.getX() + (float)corner.getWidth() / 2;
    }

    private float getCornerCentreY(ImageButton corner){
        return corner.getY() + (float)corner.getHeight() / 2;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                dX = view.getX() - event.getRawX();
                dY = view.getY() - event.getRawY();
                try {
                    pointPreview.setImageBitmap(getPointPreviewBitmap((int)view.getX(), (int)view.getY()));
                    pointPreview.setVisibility(View.VISIBLE);
                }
                catch(Exception e) {
                    Log.d(TAG, "onTouch: point preview out of the image box in action down");
                }
                break;

            case MotionEvent.ACTION_MOVE:
                view.setY(event.getRawY() + dY);
                view.setX(event.getRawX() + dX);
                drawLines();
                try {
                    pointPreview.setImageBitmap(getPointPreviewBitmap((int)view.getX(), (int)view.getY()));
                }
                catch(Exception e) {
                    Log.d(TAG, "onTouch: point preview out of the image bounds in action move");
                }
                break;

            case MotionEvent.ACTION_UP:
                pointPreview.setVisibility(View.GONE);
                break;

            default:
                return false;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        List<Point> points = getCornersCenterPoints(corners);
        savedPoints = new ArrayList<>(points);

        Mat mat = new Mat();
        Bitmap bmp32 = rotatedBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(bmp32, mat);
        Bitmap bitmap = cropToPoints(points, mat);
        bitmap = flipHorizontally(bitmap);
        Bundle bundle = new Bundle();
        bundle.putParcelable("bitmap", bitmap);
        navController.navigate(R.id.action_receiptScannerImageCropFragment_to_receiptScannerImagePreviewFragment, bundle);
    }

    private List<Point> getCornersCenterPoints(List<ImageButton> corners){
        List<Point> points = new ArrayList<>();
        corners.forEach(corner -> points.add(new Point(getCornerCentreX(corner) / SCREEN_WIDTH_SCALE, (getCornerCentreY(corner) - imageViewOffsetY) / SCREEN_HEIGHT_SCALE)));
        return points;
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

    private Bitmap flipHorizontally(Bitmap bitmap){
        Matrix matrix = new Matrix();
        matrix.postScale(-1, 1, bitmap.getWidth()/2f, bitmap.getHeight()/2f);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private void drawLines(){

        Point topLeft = new Point(getCornerCentreX(corners.get(0)), getCornerCentreY(corners.get(0)));
        Point topRight = new Point(getCornerCentreX(corners.get(1)), getCornerCentreY(corners.get(1)));
        Point bottomRight = new Point(getCornerCentreX(corners.get(2)), getCornerCentreY(corners.get(2)));
        Point bottomLeft = new Point(getCornerCentreX(corners.get(3)), getCornerCentreY(corners.get(3)));

        Path topPath = new Path();
        topPath.moveTo((float)topLeft.x, (float)topLeft.y);
        topPath.lineTo((float)topRight.x, (float)topRight.y);

        Path rightPath = new Path();
        rightPath.moveTo((float)topRight.x, (float)topRight.y);
        rightPath.lineTo((float)bottomRight.x, (float)bottomRight.y);

        Path bottomPath = new Path();
        bottomPath.moveTo((float)bottomRight.x, (float)bottomRight.y);
        bottomPath.lineTo((float)bottomLeft.x, (float)bottomLeft.y);

        Path leftPath = new Path();
        leftPath.moveTo((float)bottomLeft.x, (float)bottomLeft.y);
        leftPath.lineTo((float)topLeft.x, (float)topLeft.y);

        canvas = surfaceView.getHolder().lockCanvas();
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        canvas.drawPath(topPath, primaryColorPaint);
        canvas.drawPath(rightPath, primaryColorPaint);
        canvas.drawPath(bottomPath, primaryColorPaint);
        canvas.drawPath(leftPath, primaryColorPaint);
        surfaceView.getHolder().unlockCanvasAndPost(canvas);
    }

    private Bitmap getPointPreviewBitmap(int x, int y){
        float correctedX = (float)x / SCREEN_WIDTH_SCALE;
        float correctedY = ((float)y - imageViewOffsetY) / SCREEN_HEIGHT_SCALE;
        float correctedWidth = (float)corners.get(0).getWidth() / SCREEN_WIDTH_SCALE;
        float correctedHeight = (float)corners.get(0).getHeight() / SCREEN_HEIGHT_SCALE;
        Bitmap bitmap = Bitmap.createBitmap(rotatedBitmap, (int)correctedX, (int)correctedY, (int)correctedWidth, (int)correctedHeight, null, true);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, 10, accentColorPaint);
        return bitmap;
    }

}