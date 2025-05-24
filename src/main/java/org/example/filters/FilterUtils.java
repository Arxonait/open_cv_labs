package org.example.filters;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class FilterUtils {

    public static FilterResults applyFilters(String imagePath, int kernelSize) {
        Mat src = Imgcodecs.imread(imagePath);
        if (src.empty()) {
            System.err.println("Cannot read image: " + imagePath);
            return null;
        }

        Size ksize = new Size(kernelSize, kernelSize);

        // Blur
        Mat blur = new Mat();
        Imgproc.blur(src, blur, ksize);

        // Gaussian
        Mat gaussian = new Mat();
        Imgproc.GaussianBlur(src, gaussian, ksize, 0);

        // Median
        Mat median = new Mat();
        if (kernelSize % 2 == 1 && kernelSize > 1) {
            Imgproc.medianBlur(src, median, kernelSize);
        } else {
            median = src.clone(); // если невалидный размер ядра, вернем копию
        }

        // Bilateral
        Mat bilateral = new Mat();
        Imgproc.bilateralFilter(src, bilateral, kernelSize, 75, 75);

        return new FilterResults(blur, gaussian, median, bilateral);
    }
}
