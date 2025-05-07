package org.example;

import org.junit.BeforeClass;
import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ImageApiTransformationsTest {

    private static ImageAPI api;
    private static String srcImage;
    private static String outputDir;
    private static Mat original;

    @BeforeClass
    public static void setUpClass() throws Exception {
        api = new ImageAPI();
        srcImage = "src/test/test_dir/car.jpeg";
        outputDir = "/home/vboxuser/Pictures/image_transformations_results_3/";

        Files.createDirectories(Paths.get(outputDir));
        original = api.loadImage(srcImage);
        api.showImage(original);
        api.saveImage(outputDir + "original.jpeg", original);
    }

    @Test
    public void testSobelX() {
        Mat sobelX = api.applySobel(original, 1, 0, 3, 1, 0, Core.BORDER_DEFAULT);
        api.saveImage(outputDir + "sobelX.jpeg", sobelX);
    }

    @Test
    public void testSobelY() {
        Mat sobelY = api.applySobel(original, 0, 1, 3, 1, 0, Core.BORDER_DEFAULT);
        api.saveImage(outputDir + "sobelY.jpeg", sobelY);
    }

    @Test
    public void testFlipHorizontal() {
        Mat flipped = api.flip(original, true);
        api.saveImage(outputDir + "flipped_horizontal.jpeg", flipped);
    }

    @Test
    public void testFlipVertical() {
        Mat flipped = api.flip(original, false);
        api.saveImage(outputDir + "flipped_vertical.jpeg", flipped);
    }

    @Test
    public void testRepeatImage() {
        int ny = 2, nx = 2;
        Mat repeated = api.repeat(original, ny, nx);
        assertNotNull(repeated);
        assertFalse(repeated.empty());

        // Проверяем, что размер увеличился в nx и ny раз
        Size originalSize = original.size();
        Size repeatedSize = repeated.size();
        assertEquals(originalSize.width * nx, repeatedSize.width, 0.0);
        assertEquals(originalSize.height * ny, repeatedSize.height, 0.0);

        api.saveImage(outputDir + "repeated_2x2.jpeg", repeated);
    }

    @Test
    public void testConcatImagesHorizontally() {
        // Создаем список из двух одинаковых изображений
        List<Mat> images = Arrays.asList(original, original);
        Mat concatenated = api.concatImages(images, true);

        assertNotNull(concatenated);
        assertFalse(concatenated.empty());

        // При горизонтальной конкатенации ширина должна быть суммой, высота та же
        Size originalSize = original.size();
        Size concatSize = concatenated.size();
        assertEquals(originalSize.width * 2, concatSize.width, 0.0);
        assertEquals(originalSize.height, concatSize.height, 0.0);

        api.saveImage(outputDir + "concatenated_horizontal.jpeg", concatenated);
    }

    @Test
    public void testConcatImagesVertically() {
        // Создаем список из двух одинаковых изображений
        List<Mat> images = Arrays.asList(original, original);
        Mat concatenated = api.concatImages(images, false);

        assertNotNull(concatenated);
        assertFalse(concatenated.empty());

        // При вертикальной конкатенации высота должна быть суммой, ширина та же
        Size originalSize = original.size();
        Size concatSize = concatenated.size();
        assertEquals(originalSize.width, concatSize.width, 0.0);
        assertEquals(originalSize.height * 2, concatSize.height, 0.0);

        api.saveImage(outputDir + "concatenated_vertical.jpeg", concatenated);
    }

    @Test
    public void testResizeImage() {
        int newWidth = original.width() / 2;
        int newHeight = original.height() / 2;
        Mat resized = api.resize(original, newWidth, newHeight);

        assertNotNull(resized);
        assertFalse(resized.empty());
        assertEquals(newWidth, resized.width());
        assertEquals(newHeight, resized.height());

        api.saveImage(outputDir + "resized_half.jpeg", resized);
    }

    @Test
    public void testRotateImageWithContent() {
        double angle = 45.0;
        Mat rotated = api.rotate(original, angle, true);

        assertNotNull(rotated);
        assertFalse(rotated.empty());
        // При повороте с сохранением содержимого размер может измениться
        api.saveImage(outputDir + "rotated_45_with_content.jpeg", rotated);
    }

    @Test
    public void testRotateImageWithoutContent() {
        double angle = 45.0;
        Mat rotated = api.rotate(original, angle, false);

        assertNotNull(rotated);
        assertFalse(rotated.empty());

        api.saveImage(outputDir + "rotated_45_no_content.jpeg", rotated);
    }

    @Test
    public void testShiftImage() {
        int shiftX = 50, shiftY = 30;
        Mat shifted = api.shift(original, shiftX, shiftY);

        assertNotNull(shifted);
        assertFalse(shifted.empty());
        assertEquals(original.size(), shifted.size());

        api.saveImage(outputDir + "shifted_50x30.jpeg", shifted);
    }

}
