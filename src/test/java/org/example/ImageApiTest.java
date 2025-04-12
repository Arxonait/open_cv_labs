package org.example;

import org.junit.Test;
import org.opencv.core.Mat;

public class ImageApiTest {
    @Test
    public void testInit() throws Exception {
        ImageAPI api = new ImageAPI();
    }

    @Test
    public void testImageApiChannelZeroing() throws Exception {
        ImageAPI api = new ImageAPI();

        String src = "/home/vboxuser/Pictures/car.jpeg";
        String dst = "/home/vboxuser/Pictures/car_result.jpeg";

        Mat img = api.loadImage(src);
        api.showImage(img);

        Mat modified = api.zeroChannel(img, 0); // обнуляем синий канал
        api.showImage(modified);

        api.saveImage(dst, modified);

        Thread.sleep(10000);
    }

}
