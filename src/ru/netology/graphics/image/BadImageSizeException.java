package ru.netology.graphics.image;

import java.nio.charset.Charset;

public class BadImageSizeException extends Exception {
    public BadImageSizeException(double ratio, double maxRatio) {
//        Charset w1251 = Charset.forName("Windows-1251");
//        Charset utf8 = Charset.forName("UTF-8");
//        String str = "ћаксимальное соотношение сторон изображени€ ";
//        byte[] bytes = str.getBytes(Charset.forName("UTF-8"));
//        String  str = new String("ћаксимальное соотношение сторон изображени€ ", utf8);
        super("ћаксимальное соотношение сторон изображени€ " + maxRatio + ", а у этой " + ratio);
    }
}
