package org.example;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;
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

    public static int countRectangularObjects(Mat image, int targetWidth, int targetHeight, double tolerance) {
        Mat gray = new Mat();
        Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY);

        Mat binary = new Mat();
        Imgproc.threshold(gray, binary, 0, 255, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU);

        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(binary, contours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        int count = 0;
        for (MatOfPoint contour : contours) {
            Rect rect = Imgproc.boundingRect(contour);
            double width = rect.width;
            double height = rect.height;

            if (Math.abs(width - targetWidth) < tolerance && Math.abs(height - targetHeight) < tolerance) {
                count++;
                // Mat sub = image.submat(rect);
                // Imgcodecs.imwrite("rect_" + count + ".jpg", sub);
            }
        }
        return count;
    }
}
