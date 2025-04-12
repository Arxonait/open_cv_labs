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
        if (channel < 0 || channel > 2) {
            throw new IllegalArgumentException("Канал должен быть в диапазоне 0–2");
        }

        Mat result = srcMat.clone();

        int totalBytes = (int) (result.total() * result.elemSize());
        byte[] buffer = new byte[totalBytes];

        result.get(0, 0, buffer);

        // Удаление указанного канала
        for (int i = 0; i < totalBytes; i++) {
            if (i % 3 == channel) {
                buffer[i] = 0;
            }
        }

        result.put(0, 0, buffer);
        return result;
    }

    /**
     * Показывает изображение в отдельном окне, не закрывая его автоматически.
     *
     * @param mat Изображение в формате Mat
     */
    public void showImage(Mat mat) {
        int type = mat.channels() > 1 ? BufferedImage.TYPE_3BYTE_BGR : BufferedImage.TYPE_BYTE_GRAY;
        int bufferSize = mat.channels() * mat.cols() * mat.rows();
        byte[] b = new byte[bufferSize];
        mat.get(0, 0, b);

        BufferedImage image = new BufferedImage(mat.cols(), mat.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);

        ImageIcon icon = new ImageIcon(image);
        JFrame frame = new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize(image.getWidth() + 50, image.getHeight() + 50);
        frame.add(new JLabel(icon));
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // окно остаётся, пока пользователь не закроет
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