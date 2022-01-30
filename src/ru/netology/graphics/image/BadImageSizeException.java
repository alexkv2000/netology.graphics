package ru.netology.graphics.image;

public class BadImageSizeException extends Exception {
    public BadImageSizeException(double ratio, double maxRatio) {
        super("������������ ����������� ������ ����������� " + maxRatio + ", � � ���� " + ratio);
    }
}
