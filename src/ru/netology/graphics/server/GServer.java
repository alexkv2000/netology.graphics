package ru.netology.graphics.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import ru.netology.graphics.image.Convert;
import ru.netology.graphics.image.TextGraphicsConverter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * ������ ��� �� ��� �������, ��� ������� �� ���� :)
*/
public class GServer {
    public static final int PORT = 8888;

    private HttpServer server;
    private TextGraphicsConverter converter;

    public GServer(Convert converter) throws Exception {
        if (converter == null) {
            System.err.println("������� ����� ������-���������, ������ �� null.");
            return;
        }
        this.converter = converter;
        this.converter.setMaxHeight(converter.getMaxHeight());
        this.converter.setMaxWidth(converter.getMaxWidth());
        this.converter.setMaxRatio(converter.getMaxRatio());

        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/", this::serveHtml);
        server.createContext("/convert", this::serveConvertion);
    }

    public void start() {
        System.out.println("��������� ������ �� ����� " + PORT);
        System.out.println("������ � �������� http://localhost:8888/");
        server.start();
    }

    protected void serveHtml(HttpExchange h) throws IOException {
        System.out.println("Serving html..");
        var htmlPath = Path.of("assets/index.html");
        var htmlContent = Files.readString(htmlPath);
        var jsPath = Path.of("assets/my.js");
        var jsContent = Files.readString(jsPath);
        htmlContent = htmlContent.replace("{{{JS}}}", jsContent);
        var htmlBytes = htmlContent.getBytes();
        h.sendResponseHeaders(200, htmlBytes.length);
        h.getResponseBody().write(htmlBytes);
        h.close();
    }

    protected void serveConvertion(HttpExchange h) throws IOException {
        System.out.println("Convert request..");
        var url = new BufferedReader(new InputStreamReader(h.getRequestBody())).readLine();
        try {
            System.out.println("Converting image: " + url);
            Files.write(Path.of("assets/img.txt"), converter.convert(url).getBytes());
            var img = converter.convert(url).getBytes();
            System.out.println("...converted!");
            h.sendResponseHeaders(200, img.length);
            h.getResponseBody().write(img);
        } catch (Exception e) {
            var msg = e.getMessage();
            if (msg.isEmpty()) {
                msg = "��������� ������ ����������� :(" + msg;
            }
            var msgBytes = msg.getBytes();
            h.sendResponseHeaders(500, msgBytes.length);
            h.getResponseBody().write(msgBytes);
        } finally {
            h.close();
        }
    }
}
