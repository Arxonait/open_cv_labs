package org.example;

import org.junit.BeforeClass;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

public class ImageApiTransformationsTest {

    private static ImageAPI api;
    private static String srcImage;
    private static String outputDir;
    private static Mat original;

    @BeforeClass
    public static void setUpClass() throws Exception {
        api = new ImageAPI();
        srcImage = "/home/vboxuser/Pictures/car.jpeg";
        outputDir = "/home/vboxuser/Pictures/image_transformations_results/";

        Files.createDirectories(Paths.get(outputDir));
        original = api.loadImage(srcImage);
        api.showImage(original);
        api.saveImage(outputDir + "original.jpeg", original);
    }

    @Test
    public void testFlipHorizontal() {
        Mat flipped = api.flip(original, true);
        assertNotNull(flipped);
        assertFalse(flipped.empty());
        assertNotEquals(original.size(), flipped.size()); // Размер должен остаться тем же
        api.saveImage(outputDir + "flipped_horizontal.jpeg", flipped);
    }

    @Test
    public void testFlipVertical() {
        Mat flipped = api.flip(original, false);
        assertNotNull(flipped);
        assertFalse(flipped.empty());
        assertEquals(original.size(), flipped.size());
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
        double angle = 90.0;
        Mat rotated = api.rotate(original, angle, false);

        assertNotNull(rotated);
        assertFalse(rotated.empty());
        // При повороте на 90 градусов без сохранения контента ширина и высота меняются местами
        assertEquals(original.height(), rotated.width());
        assertEquals(original.width(), rotated.height());

        api.saveImage(outputDir + "rotated_90_no_content.jpeg", rotated);
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
