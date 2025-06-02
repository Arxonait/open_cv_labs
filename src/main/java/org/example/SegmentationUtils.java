package org.example;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.Random;

public class SegmentationUtils {

    public static void floodFillWithParams(Mat image, Point seedPoint, Scalar fillColor, Scalar loDiff, Scalar upDiff) {
        if (fillColor == null) {
            Random rand = new Random();
            fillColor = new Scalar(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
        }
        if (loDiff == null || upDiff == null) {
            loDiff = new Scalar(30, 30, 30);
            upDiff = new Scalar(30, 30, 30);
        }
        Mat mask = new Mat();
        Imgproc.floodFill(image, mask, seedPoint, fillColor, new Rect(), loDiff, upDiff, Imgproc.FLOODFILL_FIXED_RANGE);
    }

    public static Mat applyPyramidDown(Mat image, int times) {
        Mat result = image.clone();
        for (int i = 0; i < times; i++) {
            Mat temp = new Mat();
            Imgproc.pyrDown(result, temp);
            result = temp;
        }
        return result;
    }

    public static Mat applyPyramidUp(Mat image, int times) {
        Mat result = image.clone();
        for (int i = 0; i < times; i++) {
            Mat temp = new Mat();
            Imgproc.pyrUp(result, temp);
            result = temp;
        }
        return result;
    }

    public static Mat getImageDifference(Mat original, Mat processed) {
        Mat result = new Mat();
        Core.subtract(original, processed, result);
        return result;
    }
}
