package org.example;

import org.opencv.core.Core;
import org.apache.log4j.Logger;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;

import java.util.Locale;
import java.util.List;

public class ImageAPI {
    private static final Logger log = Logger.getLogger(ImageAPI.class);
    private final ImageProcessor processor;
    private final ImageTransformations transformations;

    public ImageAPI() throws Exception {
        processor = new ImageProcessor();
        transformations = new ImageTransformations();
        log.info("Checking OS.....");

        switch (getOperatingSystemType()) {
            case LINUX:
                log.info("OS Linux");
                System.load(Config.getProp(Constants.PATH_TO_NATIVE_LIB_LINUX));
                break;
            case WINDOWS:
                log.info("OS Windows");
                System.load(Config.getProp(Constants.PATH_TO_NATIVE_LIB_WIN));
                break;
            case MACOS:
                throw new Exception("Mac OS does not support!!!!!!!!");
            case OTHER:
                throw new Exception("Current OS does not support!!!!!");
        }

        log.info("OpenCV version - " + Core.getVersionString());
    }

    private Constants.OSType getOperatingSystemType() {
        String os = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
        if (os.contains("mac") || os.contains("darwin")) return Constants.OSType.MACOS;
        else if (os.contains("win")) return Constants.OSType.WINDOWS;
        else if (os.contains("nux")) return Constants.OSType.LINUX;
        else return Constants.OSType.OTHER;
    }

    public Mat loadImage(String path) {
        log.info("Loading image from " + path);
        return processor.loadImage(path);
    }

    public Mat zeroChannel(Mat img, int channel) {
        log.info("Zeroing channel " + channel);
        return processor.zeroChannel(img, channel);
    }

    public void showImage(Mat img) {
        log.info("Displaying image");
        processor.showImage(img);
    }

    public void saveImage(String path, Mat img) {
        log.info("Saving image to " + path);
        processor.saveImage(path, img);
    }

    public Mat applySobel(Mat img, int dx, int dy, int ksize,
                          double scale, double delta, int borderType) {
        log.info("Applying Sobel operator with dx=" + dx + ", dy=" + dy + ", kernel size: " + ksize + ", scale: " + scale +
                ", delta: " + delta + ", border type: " + borderType);
        return processor.applySobel(img, dx, dy, ksize, scale, delta, borderType);
    }

    public Mat applyLaplace(Mat img, int ksize, double scale, double delta, int borderType) {
        log.info("Applying Laplace operator with" + "ksize=" + ksize + ", " + "scale=" + scale + ", " +
                "delta=" + delta + ", " +
                "borderType=" + borderType);
        return processor.applyLaplacian(img, ksize, scale, delta, borderType);
    }

    public Mat flip(Mat img, boolean horizontal) {
        log.info("Flipping image horizontal " + horizontal);
        return processor.flipImage(img, horizontal);
    }

    public Mat repeat(Mat img, int ny, int nx) {
        log.info("Repeating image ny=" + ny + ", nx=" + nx);
        return processor.repeatImage(img, ny, nx);
    }

    public Mat concatImages(List<Mat> images, boolean horizontal) {
        log.info("Concatenating images. Horizontal = " + horizontal);
        return processor.concatImages(images, horizontal);
    }

    public Mat resize(Mat img, int width, int height) {
        log.info("Resizing image to " + width + "x" + height);
        return processor.resizeImage(img, width, height);
    }

    public Mat rotate(Mat img, double angle, boolean keepContent) {
        log.info("Rotating image by " + angle + " degrees, keepContent = " + keepContent);
        return processor.rotateImage(img, angle, keepContent);
    }

    public Mat shift(Mat img, int shiftX, int shiftY) {
        log.info("Shifting image by X=" + shiftX + ", Y=" + shiftY);
        return processor.shiftImage(img, shiftX, shiftY);
    }

    /**
     * Трансформация перспективы по заданному отклонению (углу)
     * @param img исходное изображение
     * @param angle угол отклонения (в градусах)
     * @param direction направление отклонения (TOP, BOTTOM, LEFT, RIGHT)
     * @param intensity интенсивность эффекта (0-1)
     * @return преобразованное изображение
     */
    public Mat perspectiveTransform(Mat img, double angle, String direction, double intensity) {
        log.info("Applying perspective transformation");
        return processor.transformPerspectiveWithDeviation(img, angle, direction, intensity);
    }

    // ---

    public Mat floodFillWithParams(Mat image, Point seedPoint, Scalar fillColor, Scalar loDiff, Scalar upDiff) {
        log.info("Applying flood fill image");
        SegmentationUtils.floodFillWithParams(image, seedPoint, fillColor, loDiff, upDiff);
        return image;
    }

    public Mat applyPyramidDown(Mat image, int times) {
        log.info("Applying pyramid down image");
        SegmentationUtils.applyPyramidDown(image, times);
        return image;
    }

    public Mat applyPyramidUp(Mat image, int times) {
        log.info("Applying pyramid up image");
        SegmentationUtils.applyPyramidUp(image, times);
        return image;
    }

    public Mat getImageDifference(Mat original, Mat processed) {
        log.info("Get difference image");
        Mat image = SegmentationUtils.getImageDifference(original, processed);
        return image;
    }

    public int getCountObj(Mat image, int targetWidth, int targetHeight, double tolerance) {
        log.info("Get count rectangular objects from image");
        return SegmentationUtils.countRectangularObjects(image, targetWidth, targetHeight, tolerance);
    }
}
