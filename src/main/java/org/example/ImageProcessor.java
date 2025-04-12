package org.example;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class ImageProcessor {

    /**
     * Загружает изображение из указанного пути в объект Mat.
     *
     * @param path Полный путь до изображения
     * @return Объект Mat с изображением
     */
    public Mat loadImage(String path) {
        Mat image = Imgcodecs.imread(path);
        if (image.empty()) {
            throw new IllegalArgumentException("Не удалось загрузить изображение по пути: " + path);
        }
        return image;
    }

    /**
     * Обнуляет указанный канал (0 — синий, 1 — зеленый, 2 — красный) в изображении.
     *
     * @param srcMat  Матрица исходного изображения (Mat)
     * @param channel Номер канала (0, 1 или 2)
     * @return Новая Mat-матрица с обнуленным каналом
     */
    public Mat zeroChannel(Mat srcMat, int channel) {
        // Проверка на корректность номера канала
        if (channel < 0 || channel > 2) {
            throw new IllegalArgumentException("Канал должен быть в диапазоне 0–2");
        }

        // Клонируем исходное изображение, чтобы не изменять оригинал
        Mat result = srcMat.clone();

        // Вычисляем общее количество байт: общее число пикселей * размер каждого элемента
        int totalBytes = (int) (result.total() * result.elemSize());
        byte[] buffer = new byte[totalBytes]; // Буфер для хранения всех пикселей

        // Загружаем данные изображения в буфер
        result.get(0, 0, buffer);

        // Проходим по всем пикселям и обнуляем выбранный канал
        // Поскольку каждый пиксель представлен 3 байтами (B, G, R),
        // индексы каналов будут чередоваться: 0, 1, 2, 0, 1, 2 и т.д.
        for (int i = 0; i < totalBytes; i++) {
            if (i % 3 == channel) {
                buffer[i] = 0; // Обнуляем нужный канал
            }
        }

        // Записываем изменённые данные обратно в матрицу изображения
        result.put(0, 0, buffer);

        // Возвращаем изменённую копию изображения
        return result;
    }

    /**
     * Показывает изображение в отдельном окне, не закрывая его автоматически.
     *
     * @param mat Изображение в формате Mat
     */
    public void showImage(Mat mat) {
        // Определяем тип изображения: цветное (3 канала) или оттенки серого (1 канал)
        int type = mat.channels() > 1
                ? BufferedImage.TYPE_3BYTE_BGR   // Цветное изображение (формат BGR для OpenCV)
                : BufferedImage.TYPE_BYTE_GRAY;  // Градации серого

        // Расчёт общего количества байт: количество каналов * ширина * высота
        int bufferSize = mat.channels() * mat.cols() * mat.rows();

        // Создаём буфер для хранения байт изображения
        byte[] b = new byte[bufferSize];

        // Копируем данные из объекта Mat в массив байтов
        mat.get(0, 0, b);

        // Создаём объект BufferedImage с нужной шириной, высотой и типом
        BufferedImage image = new BufferedImage(mat.cols(), mat.rows(), type);

        // Получаем массив байтов, связанный с пикселями BufferedImage
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();

        // Копируем содержимое изображения из OpenCV (Mat) в объект BufferedImage
        System.arraycopy(b, 0, targetPixels, 0, b.length);

        // Создаём иконку из изображения для отображения в Swing-интерфейсе
        ImageIcon icon = new ImageIcon(image);

        // Создаём окно с компонентами для отображения изображения
        JFrame frame = new JFrame();
        frame.setLayout(new FlowLayout()); // Простое выравнивание
        frame.setSize(image.getWidth() + 50, image.getHeight() + 50); // Задаём размер окна

        // Добавляем изображение в окно через JLabel
        frame.add(new JLabel(icon));

        // Делаем окно видимым
        frame.setVisible(true);

        // Устанавливаем поведение при закрытии окна: только закрытие окна, а не всей программы
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }


    /**
     * Сохраняет изображение в файл.
     *
     * @param path Путь для сохранения
     * @param mat  Матрица изображения
     */
    public void saveImage(String path, Mat mat) {
        Imgcodecs.imwrite(path, mat);
    }
}