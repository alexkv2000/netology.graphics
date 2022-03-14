package ru.netology.graphics.image;

import java.nio.charset.Charset;

public class BadImageSizeException extends Exception {
    public BadImageSizeException(double ratio, double maxRatio) {
//        Charset w1251 = Charset.forName("Windows-1251");
//        Charset utf8 = Charset.forName("UTF-8");
//        String str = "������������ ����������� ������ ����������� ";
//        byte[] bytes = str.getBytes(Charset.forName("UTF-8"));
//        String  str = new String("������������ ����������� ������ ����������� ", utf8);
        super("������������ ����������� ������ ����������� " + maxRatio + ", � � ���� " + ratio);
    }
}
