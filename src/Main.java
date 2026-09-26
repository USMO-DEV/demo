import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Main {

    private static final int PORT = 8080;
    private static final Path WEB_DIR = Path.of("web");

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        // 首页 / 静态页面
        server.createContext("/", new StaticFileHandler());

        // 简单的 JSON 接口
        server.createContext("/api/time", exchange -> {
            String json = "{\"time\": \"" + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "\"}";
            send(exchange, 200, json, "application/json; charset=utf-8");
        });

        server.setExecutor(null);
        server.start();
        System.out.println("服务已启动: http://localhost:" + PORT);
    }

    static class StaticFileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            if ("/".equals(path)) {
                path = "/index.html";
            }
            Path file = WEB_DIR.resolve(path.substring(1)).normalize();
            if (!file.startsWith(WEB_DIR) || !Files.exists(file)) {
                send(exchange, 404, "404 Not Found", "text/plain; charset=utf-8");
                return;
            }
            String type = contentType(file.getFileName().toString());
            byte[] bytes = Files.readAllBytes(file);
            exchange.getResponseHeaders().set("Content-Type", type);
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
    }

    private static String contentType(String name) {
        if (name.endsWith(".html")) return "text/html; charset=utf-8";
        if (name.endsWith(".css")) return "text/css; charset=utf-8";
        if (name.endsWith(".js")) return "application/javascript; charset=utf-8";
        if (name.endsWith(".png")) return "image/png";
        if (name.endsWith(".jpg")) return "image/jpeg";
        return "application/octet-stream";
    }

    private static void send(HttpExchange exchange, int code, String body, String type) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", type);
        exchange.sendResponseHeaders(code, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
