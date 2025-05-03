package org.example;

import org.opencv.core.Core;
import org.apache.log4j.Logger;
import org.opencv.core.Mat;

import java.util.Locale;

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

    // --- Методы для трансформаций через API ---
    public Mat applySobel(Mat img, int dx, int dy) {
        log.info("Applying Sobel operator with dx=" + dx + ", dy=" + dy);
        return transformations.applySobel(img, dx, dy);
    }

    public Mat applyLaplace(Mat img) {
        log.info("Applying Laplace operator");
        return transformations.applyLaplace(img);
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

    public Mat perspectiveTransform(Mat img) {
        log.info("Applying perspective transformation");
        return transformations.perspectiveTransform(img);
    }
}
