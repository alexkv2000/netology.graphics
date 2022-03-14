package ru.netology.graphics;

import ru.netology.graphics.image.Convert;
import ru.netology.graphics.image.SchemaImage;
import ru.netology.graphics.image.TextColorSchema;
import ru.netology.graphics.image.TextGraphicsConverter;
import ru.netology.graphics.server.GServer;

import java.io.File;
import java.io.PrintWriter;

public class Main {
    public static void main(String[] args) throws Exception {
        Convert converter = new Convert(600, 1200, 0, new SchemaImage()); // Создайте тут объект вашего класса конвертера
        GServer server = new GServer(converter); // Создаём объект сервера
        server.start(); // Запускаем*/
        // Или то же, но с сохранением в файл:
        PrintWriter fileWriter = new PrintWriter(new File("converted-image.txt"));
        converter.setMaxWidth(300);
        converter.setMaxHeight(600);
        converter.setMaxRatio(0);
        converter.setTextColorSchema(". , : \" ! ? % № $ # @");
        //  https://invigor.by/assets/images/resources/191/3877-o.jpg
        try {
            fileWriter.write(converter.convert("https://img.favcars.com/audi/r8/pictures_audi_r8_2007_8_1024x768.jpg"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        fileWriter.close();
    }
}
