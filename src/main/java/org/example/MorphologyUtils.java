package org.example;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class MorphologyUtils {

    /**
     * Применяет морфологическую операцию к изображению.
     *
     * @param imagePath  Путь к изображению (в формате строки).
     * @param kernelSize Размер ядра (ширина и высота должны быть равны и больше 0).
     * @param morphType  Тип морфологической операции (например, MORPH_GRADIENT, MORPH_BLACKHAT и др.).
     *                   Используются константы из класса Imgproc, например: Imgproc.MORPH_GRADIENT.
     * @param shapeType  Форма структурирующего элемента (например, MORPH_RECT, MORPH_ELLIPSE).
     *                   Используются константы из класса Imgproc, например: Imgproc.MORPH_RECT.
     * @return Матрица изображения (Mat) после применения морфологической операции.
     *         Возвращает null, если изображение не удалось загрузить.
     */
    public static Mat applyMorphology(String imagePath, int kernelSize, int morphType, int shapeType) {
        // Загружаем изображение
        Mat src = Imgcodecs.imread(imagePath);
        if (src.empty()) {
            System.err.println("Не удалось загрузить изображение: " + imagePath);
            return null;
        }

        // Создание структурирующего элемента заданной формы и размера
        Size ksize = new Size(kernelSize, kernelSize);
        Mat kernel = Imgproc.getStructuringElement(shapeType, ksize); // Создаёт структурирующий элемент (ядро свёртки) заданной формы (shapeType) и размера (ksize).
        Mat dst = new Mat();

        // Применение морфологической операции
        Imgproc.morphologyEx(src, dst, morphType, kernel);

        // Возвращаем результат
        return dst;
    }
}
