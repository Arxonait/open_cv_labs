package org.example;

import org.junit.BeforeClass;
import org.junit.Test;
import org.opencv.core.Mat;

import java.nio.file.Files;
import java.nio.file.Paths;

public class ImageApiChannelTest {
    private static ImageAPI api;
    private static String srcImage;
    private static String outputDir;
    private static Mat original;

    @BeforeClass
    public static void setUpClass() throws Exception {
        api = new ImageAPI();
        srcImage = "/home/vboxuser/Pictures/car.jpeg";
        outputDir = "/home/vboxuser/Pictures/image_processing_results/";

        Files.createDirectories(Paths.get(outputDir));
        original = api.loadImage(srcImage);
        api.showImage(original);
        api.saveImage(outputDir + "original.jpeg", original);
    }

    @Test
    public void testRedChannelZeroing() throws Exception {
        processAndSaveChannel(0, "red");
        Thread.sleep(5000); // Ждем 5 секунд
    }

    @Test
    public void testGreenChannelZeroing() throws Exception {
        processAndSaveChannel(1, "green");
        Thread.sleep(5000); // Ждем 5 секунд
    }

    @Test
    public void testBlueChannelZeroing() throws Exception {
        processAndSaveChannel(2, "blue");
        Thread.sleep(5000); // Ждем 5 секунд
    }

    private void processAndSaveChannel(int channel, String channelName) {
        Mat modified = api.zeroChannel(original.clone(), channel);
        api.showImage(modified);

        String outputPath = String.format("%sresult_%s_channel.jpeg", outputDir, channelName);
        api.saveImage(outputPath, modified);
    }
}
