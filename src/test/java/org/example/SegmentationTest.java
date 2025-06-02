package org.example;

import org.junit.BeforeClass;
import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class SegmentationTest {

    private static ImageAPI api;
    private static String srcImage;
    private static String outputDir;
    private static Mat original;

    @BeforeClass
    public static void setUpClass() throws Exception {
        api = new ImageAPI();
        srcImage = "src/test/test_dir/car.jpeg";
        outputDir = "src/test/test_dir/image_segmentation/";

        Files.createDirectories(Paths.get(outputDir));
        original = api.loadImage(srcImage);
        api.showImage(original);
        api.saveImage(outputDir + "original.jpeg", original);
    }

    @Test
    public void testFloodFill() {
        Mat image = api.floodFillWithParams(original, new Point(0, 0), null, null, null);
        api.saveImage(outputDir + "car_floodFilled.jpeg", image);
    }

    @Test
    public void testPyUp() {
        Mat image = api.applyPyramidUp(original, 2);
        api.saveImage(outputDir + "car_py_up.jpeg", image);
        Mat diff = api.getImageDifference(original, image);
        api.saveImage(outputDir + "car_py_up_diff.jpeg", diff);
    }

    @Test
    public void testPyDown() {
        Mat image = api.applyPyramidDown(original, 2);
        api.saveImage(outputDir + "car_py_down.jpeg", image);
        Mat diff = api.getImageDifference(original, image);
        api.saveImage(outputDir + "car_py_down_diff.jpeg", diff);
    }

    @Test
    public void testCountRecObj() {
        int found = api.getCountObj(original, 10, 5, 10);
        assertEquals(82, found);
    }
}
