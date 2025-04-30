package org.example;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;

import java.util.ArrayList;
import java.util.List;

public class ImageTransformations {

    /**
     * Применяет оператор Собеля к изображению.
     */
    public void sobelOperatorTest(String srcPath, String destDir) {
        Mat src = Imgcodecs.imread(srcPath, Imgcodecs.IMREAD_COLOR);
        Mat gray = new Mat();
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);

        Mat sobelX = new Mat();
        Imgproc.Sobel(gray, sobelX, CvType.CV_32F, 1, 0);

        Mat sobelY = new Mat();
        Imgproc.Sobel(gray, sobelY, CvType.CV_32F, 0, 1);

        Imgcodecs.imwrite(destDir + "/sobelX.jpg", sobelX);
        Imgcodecs.imwrite(destDir + "/sobelY.jpg", sobelY);
    }

    /**
     * Зеркальное отражение изображения.
     */
    public void flipImageTest(String srcPath, String destDir) {
        Mat src = Imgcodecs.imread(srcPath);

        Mat flipV = new Mat();
        Core.flip(src, flipV, 0); // Вертикальное отражение
        Imgcodecs.imwrite(destDir + "/flip_vertical.jpg", flipV);

        Mat flipH = new Mat();
        Core.flip(src, flipH, 1); // Горизонтальное отражение
        Imgcodecs.imwrite(destDir + "/flip_horizontal.jpg", flipH);

        Mat flipVH = new Mat();
        Core.flip(src, flipVH, -1); // Вертикальное + Горизонтальное
        Imgcodecs.imwrite(destDir + "/flip_both.jpg", flipVH);
    }

    /**
     * Изменение размера изображения.
     */
    public static void resizeImageTest(String srcPath, String destDir, int width, int height) {
        Mat src = Imgcodecs.imread(srcPath);
        Mat resized = new Mat();
        Imgproc.resize(src, resized, new Size(width, height));
        Imgcodecs.imwrite(destDir + "/resized.jpg", resized);
    }

    /**
     * Вращение изображения с обрезкой или без обрезки.
     */
    public static void rotateImageTest(String srcPath, String destDir, double angle, boolean keepContent) {
        Mat src = Imgcodecs.imread(srcPath);
        Point center = new Point(src.cols() / 2.0, src.rows() / 2.0);

        Mat rotMat = Imgproc.getRotationMatrix2D(center, angle, 1.0);

        Size size = keepContent ? new Size(src.width(), src.height()) : getRotatedSize(src, angle);

        Mat rotated = new Mat();
        Imgproc.warpAffine(src, rotated, rotMat, size, Imgproc.INTER_LINEAR, Core.BORDER_CONSTANT, new Scalar(0, 0, 0));

        Imgcodecs.imwrite(destDir + "/rotated.jpg", rotated);
    }

    /**
     * Вспомогательный метод для расчета размера без обрезки.
     */
    private static Size getRotatedSize(Mat src, double angle) {
        double radians = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(radians));
        double cos = Math.abs(Math.cos(radians));
        int newWidth = (int) (src.height() * sin + src.width() * cos);
        int newHeight = (int) (src.height() * cos + src.width() * sin);
        return new Size(newWidth, newHeight);
    }

    /**
     * Сдвиг изображения на заданное количество пикселей.
     */
    public static void shiftImageTest(String srcPath, String destDir, int shiftX, int shiftY) {
        Mat src = Imgcodecs.imread(srcPath);

        Mat translationMat = Mat.eye(2, 3, CvType.CV_32F);
        translationMat.put(0, 2, shiftX);
        translationMat.put(1, 2, shiftY);

        Mat shifted = new Mat();
        Imgproc.warpAffine(src, shifted, translationMat, src.size());
        Imgcodecs.imwrite(destDir + "/shifted.jpg", shifted);
    }

    /**
     * Трансформация перспективы изображения.
     */
    public static void perspectiveTransformTest(String srcPath, String destDir) {
        Mat src = Imgcodecs.imread(srcPath);

        Point[] srcPoints = new Point[]{
                new Point(0, 0),
                new Point(src.cols() - 1, 0),
                new Point(0, src.rows() - 1),
                new Point(src.cols() - 1, src.rows() - 1)
        };

        Point[] dstPoints = new Point[]{
                new Point(50, 50),
                new Point(src.cols() - 50, 100),
                new Point(100, src.rows() - 50),
                new Point(src.cols() - 100, src.rows() - 100)
        };

        Mat srcMat = Converters.vector_Point2f_to_Mat(List.of(srcPoints));
        Mat dstMat = Converters.vector_Point2f_to_Mat(List.of(dstPoints));

        Mat perspectiveTransform = Imgproc.getPerspectiveTransform(srcMat, dstMat);

        Mat dst = new Mat();
        Imgproc.warpPerspective(src, dst, perspectiveTransform, src.size());

        Imgcodecs.imwrite(destDir + "/perspective.jpg", dst);
    }
}
