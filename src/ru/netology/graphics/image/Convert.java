package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

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

        BufferedImage img = ImageIO.read(new URL(url)); // ������� �������� �� ���������
        // ���� ��������� ��������� ��������� �� ����������� ����������
        // ����������� ������ �����������, �� ��� ����� ���� ������� ��� ��������,

        if (isRatio()) {
            try {
                if (Math.max(img.getHeight(),img.getWidth()) / Math.min(img.getHeight(),img.getWidth()) > this.maxRatio){
                    throw new BadImageSizeException(Math.max(img.getWidth() / img.getHeight(), img.getHeight() / img.getWidth()), this.maxRatio);
                }
            } catch (BadImageSizeException e) {
                System.out.println("������ Ratio : " + e.getMessage());
                return "";
            }

        }
        // �, ���� �������� �� ��������, ��������� ���������� BadImageSizeException.
        // ����� �������� ������ ��������, �������� img.getWidth(), ������ - img.getHeight()
        // ���� ���������� ��������� ����������� ���������� ������ �/��� ������,
        // ��� ���� �� ��� � �� ������� ������ � ������ ��������� ����� ������
        // � ������.
        // ���������� ��������� �������� ��� �� ��������� ������ � ������ ������
        // � ���������� ���������� ���.
        // ������ 1: ����. ���������� 100x100, � �������� 500x200. ����� ������
        // ����� 100x40 (� 5 ��� ������).
        // ������ 2: ����. ���������� 100x30, � �������� 150x15. ����� ������
        // ����� 100x10 (� 1.5 ���� ������).
        // ���������, ������ ���������� ����� ��������� ����� �������.
        // �� ����������? �������� ������ ������������ �� ��������, �������!
        int newWidth = maxWidth;
        int newHeight = maxHeight;

        double k = Math.max((1.0 * img.getWidth() / maxWidth), (1.0 * img.getHeight() / maxHeight));
        if (k>1) {
            if (maxWidth < img.getWidth() || maxHeight < img.getHeight()) {
                newWidth = (int) (img.getWidth() / k);
                newHeight = (int) (img.getHeight() / k);
            }
        }

        // ������ ��� ���� ��������� �������� �������� ���� ������� �� �����
        // ��������� �������� ��������, ��� �� ������ �������� ������ ��������
        // �� ����� �������. � ���������� �� �������� ������ �� ����� ��������, �������
        // ������������ ����� �������� ������.
        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);
        // ������ ������� � �����-�����. ��� ����� �������� ���:
        // �������� ����� ������ �������� ������ ��������, ������� ������ ���������
        // ���������� �����-����� �������� �������:
        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bwImg.createGraphics(); // �������� � ���� �������� ���������� ��� ��������� �� ���:
        graphics.drawImage(scaledImage, 0, 0, null); // � ����� ����������� ������, ����� �� ������������ �� ����� �������� ��������:
        // ������ � bwImg � ��� ����� �����-����� �������� ������ ��� ��������.
        // �� ������ ����������� ������ �� ������, ������ � ����� ������� ���
        // ��� ������� �������� ������������� �������� � ���� �����:
        ImageIO.write(bwImg, "png", new File("out.png"));//
        // ����� ������ ���� ���������� � ��� � ������� �������� ���� �������� out.png
        // ������ ������� �������� �� �������� ������ �����������.
        // ���� ��� ��������� �� ������� � �������� .createGraphics(),
        // �� ��� ������� �� �������� ��� ����� ����� ���� ����������:
        var bwRaster = bwImg.getRaster(); //������ �� ��������
        // �� ����� ���, ��� � ���� �� ����� �������� ������� �� ������
        // ��� �����������, ������ ����� ������� (w) � ������ (h)
        // int color = bwRaster.getPixel(newWidth, newHeight, new int[3])[0];
        // �������� �������? ��������. ��� ������������ ������� ������� ���
        // ������ �� ��� �����, ������ ��� ������������� ��������, ������� � ������.
        // �� � ����� �����-����� �������� ������ ��� � ��� ����������
        // ������ ������ �������� � �������. �� ��������, � �����
        // �� ��� ���������� ������� ������� ������ �� ��� ������?
        // ���� � ��� ��� ���� ����� �� ����� ��������� ��� ��� � ������
        // ��� ������� ���, � ��� ����� ���� �������� ��� � �����.
        // ������ ��� ��������� ������� ������ ��� ��� ��������. �� ������ �������
        // ������ ���� ���, ��������� � ���������� � ���������� ����
        // � ��� �� ������ � �����, ������� ��� ����� ���������.
        // ��� �������� ����������� ������� ������ �� ���� �������� (������)
        // � ������� (������) �����������, �� ������ ���������� ��������
        // �������� ������� ������ ������� (int color ����) � �� ���
        // �������� ��������������� ������ c. ������� ����������� �����
        // � ������ ����� ���������� ������ ������, ������� �� ���������� ����
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
        // �������� ������� ��� ������� � ���� ������� �����
        // ��� ���� ����� ����������� �� ���� ������� �����, ����������
        // ������ ������� ���������� � ��� ������������� �������, ����������
        // �� �����.
        return strImage.toString(); // ���������� ��������� �����.
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
