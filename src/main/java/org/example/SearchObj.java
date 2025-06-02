package org.example;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class SearchObj {

    public static Mat detectEdges(Mat src, int blurKernelSize, double lowThreshold, double highThreshold, boolean useOtsu) {
        // Перевод в оттенки серого
        Mat gray = new Mat();
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);

        // Сглаживание (уменьшение шума)
        Mat blurred = new Mat();
        Size kernelSize = new Size(blurKernelSize, blurKernelSize);
        Imgproc.blur(gray, blurred, kernelSize);

        // Вычисление порогов
        double threshold1 = lowThreshold;
        double threshold2 = highThreshold;

        if (useOtsu) {
            Mat thresholdImage = new Mat();
            double otsuThreshold = Imgproc.threshold(gray, thresholdImage, 0, 255, Imgproc.THRESH_OTSU);
            threshold1 = otsuThreshold;
            threshold2 = otsuThreshold * 3;
        }

        // Применение Canny
        Mat edges = new Mat();
        Imgproc.Canny(blurred, edges, threshold1, threshold2);

        return edges;
    }

    public static int detectRectangles(Mat image, int targetWidth, int targetHeight, double tolerance) {
        Mat gray = new Mat();
        Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.GaussianBlur(gray, gray, new Size(5, 5), 0);
        Imgproc.Canny(gray, gray, 50, 150);

        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(gray, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        int count = 0;
        for (MatOfPoint contour : contours) {
            MatOfPoint2f contour2f = new MatOfPoint2f(contour.toArray());
            RotatedRect rect = Imgproc.minAreaRect(contour2f);
            Size size = rect.size;

            double w = Math.min(size.width, size.height);
            double h = Math.max(size.width, size.height);

            if (Math.abs(w - targetWidth) <= tolerance && Math.abs(h - targetHeight) <= tolerance) {
                count++;
            }
        }
        return count;
    }



}
