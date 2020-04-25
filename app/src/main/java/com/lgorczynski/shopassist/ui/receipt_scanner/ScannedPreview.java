package com.lgorczynski.shopassist.ui.receipt_scanner;

import android.graphics.Bitmap;
import android.os.Parcelable;

import org.opencv.core.Point;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ScannedPreview implements Serializable {

    private Bitmap bitmap;
    private List<Point> points;

    public ScannedPreview(Bitmap bitmap, List<Point> points) {
        this.bitmap = bitmap;
        this.points = points;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void correctPoints(float screenWidth, float screenHeight, float SCREEN_WIDTH_SCALE, float SCREEN_HEIGHT_SCALE){
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
}
