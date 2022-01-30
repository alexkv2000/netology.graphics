package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Convert implements TextGraphicsConverter {
    int maxWidth;
    int maxHeight;
    double maxRatio;
    TextColorSchema schema;

    public Convert(int maxWidth, int maxHeight, double maxRatio, TextColorSchema schema) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.maxRatio = maxRatio;
        this.schema = schema;
    }

    public Convert(int maxWidth, int maxHeight, double maxRatio) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.maxRatio = maxRatio;
        this.schema = new SchemaImage();
    }

    public Convert(int maxWidth, int maxHeight) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.maxRatio = 0;
        this.schema = new SchemaImage();
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public double getMaxRatio() {
        return maxRatio;
    }

    public TextColorSchema getSchema() {
        return schema;
    }

    boolean isRatio() {
        return this.maxRatio > 0;
    }

    @Override
    public String convert(String url) throws IOException {

        BufferedImage img = ImageIO.read(new URL(url)); // скачаем картинку из интернета
        // Если конвертер попросили проверять на максимально допустимое
        // соотношение сторон изображения, то вам здесь надо сделать эту проверку,

        if (isRatio()) {
            try {
                if (img.getWidth() / img.getHeight() > this.maxRatio ||
                        img.getHeight() / img.getWidth() > this.maxRatio) {
                    throw new BadImageSizeException(Math.max(img.getWidth() / img.getHeight(), img.getHeight() / img.getWidth()), this.maxRatio);
                }
            } catch (BadImageSizeException e) {
                System.out.println("Error : " + e.getMessage());
                return "";
            }

        }
        // и, если картинка не подходит, выбросить исключение BadImageSizeException.
        // Чтобы получить ширину картинки, вызовите img.getWidth(), высоту - img.getHeight()
        // Если конвертеру выставили максимально допустимые ширину и/или высоту,
        // вам надо по ним и по текущим высоте и ширине вычислить новые высоту
        // и ширину.
        // Соблюдение пропорций означает что вы уменьшать ширину и высоту должны
        // в одинаковое количество раз.
        // Пример 1: макс. допустимые 100x100, а картинка 500x200. Новый размер
        // будет 100x40 (в 5 раз меньше).
        // Пример 2: макс. допустимые 100x30, а картинка 150x15. Новый размер
        // будет 100x10 (в 1.5 раза меньше).
        // Подумайте, какими действиями можно вычислить новые размеры.
        // Не получается? Спросите вашего руководителя по курсовой, поможем!
        int newWidth = maxWidth;
        int newHeight = maxHeight;

        double k = Math.max((1.0 * img.getWidth() / maxWidth), (1.0 * img.getHeight() / maxHeight));
        if (k>1) {
            if (maxWidth < img.getWidth() || maxHeight < img.getHeight()) {
                newWidth = (int) (img.getWidth() / k);
                newHeight = (int) (img.getHeight() / k);
            }
        }

        // Теперь нам надо попросить картинку изменить свои размеры на новые
        // Последний параметр означает, что мы просим картинку плавно сузиться
        // на новые размеры. В результате мы получаем ссылку на новую картинку, которая
        // представляет собой суженную старую.
        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);
        // Теперь сделаем её чёрно-белой. Для этого поступим так:
        // Создадим новую пустую картинку нужных размеров, заранее указав последним
        // параметром чёрно-белую цветовую палитру:
        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bwImg.createGraphics(); // Попросим у этой картинки инструмент для рисования на ней:
        graphics.drawImage(scaledImage, 0, 0, null); // А этому инструменту скажем, чтобы он скопировался из нашей суженной картинки:
        // Теперь в bwImg у нас лежит чёрно-белая картинка нужных нам размеров.
        // Вы можете отслеживать каждый из этапов, просто в любом удобном для
        // вас моменте сохранив промежуточную картинку в файл через:
        ImageIO.write(bwImg, "png", new File("out.png"));//
        // После вызова этой инструкции у вас в проекте появится файл картинки out.png
        // Теперь давайте пройдёмся по пикселям нашего изображения.
        // Если для рисования мы просили у картинки .createGraphics(),
        // то для прохода по пикселям нам нужен будет этот инструмент:
        var bwRaster = bwImg.getRaster(); //проход по пикселям
        // Он хорош тем, что у него мы можем спросить пиксель на нужных
        // нам координатах, указав номер столбца (w) и строки (h)
        // int color = bwRaster.getPixel(newWidth, newHeight, new int[3])[0];
        // Выглядит странно? Согласен. Сам возвращаемый методом пиксель это
        // массив из трёх интов, обычно это интенсивность красного, зелёного и синего.
        // Но у нашей чёрно-белой картинки цветов нет и нас интересует
        // только первое значение в массиве. Вы спросите, а зачем
        // мы ещё параметром передаём интовый массив на три ячейки?
        // Дело в том что этот метод не хочет создавать его сам и просит
        // вас сделать это, а сам метод лишь заполнит его и вернёт.
        // Потому что создавать массивы каждый раз это медленно. Вы можете создать
        // массив один раз, сохранить в переменную и передавать один
        // и тот же массив в метод, ускорив тем самым программу.
        // Вам осталось пробежаться двойным циклом по всем столбцам (ширина)
        // и строкам (высота) изображения, на каждой внутренней итерации
        // получить степень белого пикселя (int color выше) и по ней
        // получить соответствующий символ c. Логикой превращения цвета
        // в символ будет заниматься другой объект, который мы рассмотрим ниже
//        char[][] tempArray = new char[maxHeight][maxWidth];

        StringBuilder strImage = new StringBuilder();
        int[] colorArray = new int[3];
        for (int i = 0; i < newHeight; i++) {
            for (int j = 0; j < newWidth; j++) {
                int color = bwRaster.getPixel(j, i, colorArray)[0];
                char c = this.schema.convert(color);
                strImage.append(c);
                strImage.append(" ");
            }
            strImage.append("\n");
        }
        // Осталось собрать все символы в один большой текст
        // Для того чтобы изображение не было слишком узким, рекомендую
        // каждый пиксель превращать в два повторяющихся символа, полученных
        // от схемы.
        return strImage.toString(); // Возвращаем собранный текст.
    }

    @Override
    public void setMaxWidth(int width) {
        this.maxWidth = width;
    }

    @Override
    public void setMaxHeight(int height) {
        this.maxHeight = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(String schema) {
        this.schema = new SchemaImage(schema);
    }

}
