package org.example;

import org.junit.BeforeClass;
import org.junit.Test;
import org.opencv.core.Mat;
import org.opencv.core.Point;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class SearchObjTest {

    private static ImageAPI api;
    private static String srcImage;
    private static String outputDir;
    private static Mat original;

    @BeforeClass
    public static void setUpClass() throws Exception {
        api = new ImageAPI();
        srcImage = "src/test/test_dir/car.jpeg";
        outputDir = "src/test/test_dir/image_search_obj/";

        Files.createDirectories(Paths.get(outputDir));
        original = api.loadImage(srcImage);
        api.showImage(original);
        api.saveImage(outputDir + "original.jpeg", original);
    }

    @Test
    public void testGetEdges() {
        Mat image = api.getImageEdges(original, 5, 50, 150, false);
        api.saveImage(outputDir + "car_edges.jpeg", image);
    }

    @Test
    public void testCountRecObj() {
        int found = api.getCountObjCanny(original, 10, 5, 10);
        assertEquals(10, found);
    }
}
