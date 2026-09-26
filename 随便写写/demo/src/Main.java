import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        // 首页：返回静态 HTML
        server.createContext("/", Main::handleIndex);
        // 简单 JSON 接口
        server.createContext("/api/hello", Main::handleHello);
        // 健康检查
        server.createContext("/api/health", ex -> respond(ex, 200, "application/json",
                "{\"status\":\"UP\"}"));

        server.setExecutor(null);
        server.start();
        System.out.println("服务已启动，请在浏览器访问: http://localhost:" + PORT);
    }

    static void handleIndex(HttpExchange ex) throws IOException {
        Path html = Path.of("web", "index.html");
        if (Files.exists(html)) {
            respond(ex, 200, "text/html; charset=utf-8", Files.readAllBytes(html));
        } else {
            respond(ex, 200, "text/html; charset=utf-8",
                    "<h1>Hello, Java!</h1><p>web/index.html 未找到</p>");
        }
    }

    static void handleHello(HttpExchange ex) throws IOException {
        String name = getQueryParam(ex.getRequestURI().getQuery(), "name");
        String json = "{\"message\":\"" + (name.isEmpty() ? "world" : name) + "\", \"time\":\""
                + java.time.LocalTime.now().withNano(0) + "\"}";
        respond(ex, 200, "application/json; charset=utf-8", json);
    }

    static String getQueryParam(String query, String key) {
        if (query == null) return "";
        for (String pair : query.split("&")) {
            String[] kv = pair.split("=", 2);
            if (kv.length == 2 && kv[0].equals(key)) return kv[1];
        }
        return "";
    }

    static void respond(HttpExchange ex, int code, String contentType, String body) throws IOException {
        respond(ex, code, contentType, body.getBytes(StandardCharsets.UTF_8));
    }

    static void respond(HttpExchange ex, int code, String contentType, byte[] body) throws IOException {
        ex.getResponseHeaders().set("Content-Type", contentType);
        ex.sendResponseHeaders(code, body.length);
        try (OutputStream os = ex.getResponseBody()) {
            os.write(body);
        }
    }
}
