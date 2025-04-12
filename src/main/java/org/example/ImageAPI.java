package org.example;

import org.opencv.core.Core;
import org.apache.log4j.Logger;
import org.opencv.core.Mat;

import java.util.Locale;

public class ImageAPI {
    private static final Logger log = Logger.getLogger(ImageAPI.class);
    private final ImageProcessor processor;

    public ImageAPI() throws Exception {
        processor = new ImageProcessor();
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
}
