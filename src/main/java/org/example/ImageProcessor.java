package org.example;

import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.List;

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

    /**
     * Применяет оператор Лапласа к изображению.
     */
    public Mat laplaceOperatorTest(Mat mat) {
        Mat gray = new Mat();
        Imgproc.cvtColor(mat, gray, Imgproc.COLOR_BGR2GRAY);

        Mat laplace = new Mat();
        Imgproc.Laplacian(gray, laplace, CvType.CV_32F);

        Mat absLaplace = new Mat();
        Core.convertScaleAbs(laplace, absLaplace);

        return absLaplace;
    }

    /**
     * Повтор изображения по вертикали и горизонтали.
     *
     * @param mat исходное изображение (Mat)
     * @param nx  количество повторений по горизонтали
     * @param ny  количество повторений по вертикали
     * @return изображение, повторённое nx раз по горизонтали и ny раз по вертикали
     */
    public Mat repeatImage(Mat mat, int nx, int ny) {
        Mat repeated = new Mat(); // Создаём выходное изображение
        Core.repeat(mat, ny, nx, repeated); // Повторяем изображение с заданным числом копий
        return repeated; // Возвращаем результат
    }

    /**
     * Объединение списка изображений в одно изображение по горизонтали или вертикали.
     *
     * @param mats       список изображений (Mat), которые нужно объединить
     * @param horizontal если true — объединить по горизонтали, иначе — по вертикали
     * @return объединённое изображение
     */
    public Mat concatImages(List<Mat> mats, boolean horizontal) {
        Mat result = new Mat(); // Создаём выходное изображение
        if (horizontal) {
            Core.hconcat(mats, result); // Объединяем изображения по горизонтали
        } else {
            Core.vconcat(mats, result); // Объединяем изображения по вертикали
        }
        return result; // Возвращаем результат
    }

    /**
     * Отразить изображение по горизонтали или вертикали.
     *
     * @param mat        исходное изображение (Mat)
     * @param horizontal если true — отражение по горизонтали, иначе — по вертикали
     * @return отражённое изображение
     */
    public Mat flipImage(Mat mat, boolean horizontal) {
        Mat flip = new Mat(); // Создаём выходное изображение
        int flag_horizontal = 1; // Флаг для отражения по горизонтали
        if (!horizontal) {
            flag_horizontal = 0; // Флаг для отражения по вертикали
        }
        Core.flip(mat, flip, flag_horizontal); // Отражаем изображение
        return flip; // Возвращаем результат
    }

    /**
     * Изменение размера изображения до заданной ширины и высоты.
     *
     * @param mat    исходное изображение (Mat)
     * @param width  новая ширина изображения
     * @param height новая высота изображения
     * @return изображение, изменённое до заданного размера
     */
    public Mat resizeImage(Mat mat, int width, int height) {
        Mat resized = new Mat(); // Создаём объект для результата
        Imgproc.resize(mat, resized, new Size(width, height)); // Изменяем размер изображения
        return resized; // Возвращаем результат
    }

    /**
     * Вращение изображения с обрезкой или без обрезки.
     */
    public Mat rotateImage(Mat mat, double angle, boolean keepContent) {
        Point center = new Point(mat.cols() / 2.0, mat.rows() / 2.0);

        Mat rotMat = Imgproc.getRotationMatrix2D(center, angle, 1.0);

        Size size = keepContent ? getRotatedSize(mat, angle) : new Size(mat.width(), mat.height());

        // Если keepContent == true, нужно сместить матрицу поворота так, чтобы центр остался в центре
        if (keepContent) {
            Size newSize = size;
            double dx = (newSize.width - mat.width()) / 2.0;
            double dy = (newSize.height - mat.height()) / 2.0;
            rotMat.put(0, 2, rotMat.get(0, 2)[0] + dx);
            rotMat.put(1, 2, rotMat.get(1, 2)[0] + dy);
        }

        Mat rotated = new Mat();
        Imgproc.warpAffine(mat, rotated, rotMat, size, Imgproc.INTER_LINEAR, Core.BORDER_CONSTANT, new Scalar(0, 0, 0));

        return rotated;
    }

    /**
     * Вспомогательный метод для расчета размера без обрезки.
     */
    private Size getRotatedSize(Mat mat, double angle) {
        double radians = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(radians));
        double cos = Math.abs(Math.cos(radians));

        int newWidth = (int) (mat.height() * sin + mat.width() * cos);
        int newHeight = (int) (mat.height() * cos + mat.width() * sin);
        return new Size(newWidth, newHeight);
    }

    /**
     * Сдвиг изображения на заданное количество пикселей по осям X и Y.
     *
     * @param mat    исходное изображение (Mat)
     * @param shiftX сдвиг по горизонтали (в пикселях). Положительное значение — вправо.
     * @param shiftY сдвиг по вертикали (в пикселях). Положительное значение — вниз.
     * @return изображение, сдвинутое на указанные значения
     */
    public Mat shiftImage(Mat mat, int shiftX, int shiftY) {

        // Создаём матрицу трансформации 2x3 для аффинного преобразования
        Mat translationMat = Mat.eye(2, 3, CvType.CV_32F); // Единичная матрица с дополнительным столбцом

        // Устанавливаем значения сдвига: shiftX — по X, shiftY — по Y
        translationMat.put(0, 2, shiftX);
        translationMat.put(1, 2, shiftY);

        Mat shifted = new Mat(); // Создаём матрицу для результата

        // Применяем аффинное преобразование к изображению
        Imgproc.warpAffine(mat, shifted, translationMat, mat.size());

        return shifted; // Возвращаем сдвинутое изображение
    }
}