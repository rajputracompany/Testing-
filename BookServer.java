import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * "The Life When She Disappeared" - full website served by plain Java (no libraries).
 * Run:  java BookServer.java [port]     then open http://localhost:8080
 * Edit book.txt and refresh the page: chapters ("## Title" then paragraphs) update live.
 */
public class BookServer {

    static String esc(String s) {
        StringBuilder b = new StringBuilder();
        for (char c : s.toCharArray()) {
            switch (c) {
                case '\\': b.append("\\\\"); break;
                case '"':  b.append("\\\""); break;
                case '\n': b.append("\\n");  break;
                case '\r': break;
                case '\t': b.append("\\t");  break;
                case '<':  b.append("\\u003c"); break;   // keeps </script> from breaking the page
                default:
                    if (c < 0x20) b.append(String.format("\\u%04x", (int) c)); else b.append(c);
            }
        }
        return b.toString();
    }

    /** Turns book.txt into the JSON array the page expects: [{"t":title,"b":[paragraphs]}, ...] */
    static String chaptersJson() throws IOException {
        String text = new String(Files.readAllBytes(Paths.get("book.txt")), StandardCharsets.UTF_8);
        StringBuilder out = new StringBuilder("[");
        boolean open = false, firstPara = true;
        for (String block : text.split("\n\n")) {
            block = block.trim();
            if (block.startsWith("## ")) {
                if (open) out.append("]},");
                out.append("{\"t\":\"").append(esc(block.substring(3))).append("\",\"b\":[");
                open = true;
                firstPara = true;
            } else if (!block.isEmpty() && open) {
                if (!firstPara) out.append(",");
                out.append("\"").append(esc(block)).append("\"");
                firstPara = false;
            }
        }
        if (open) out.append("]}");
        return out.append("]").toString();
    }

    static void send(HttpExchange ex, int code, String type, byte[] body) throws IOException {
        ex.getResponseHeaders().set("Content-Type", type);
        ex.sendResponseHeaders(code, body.length);
        try (OutputStream os = ex.getResponseBody()) { os.write(body); }
    }

    public static void main(String[] args) throws Exception {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", ex -> {
            try {
                String path = ex.getRequestURI().getPath();
                if (path.equals("/") || path.equals("/index.html")) {
                    String page = new String(Files.readAllBytes(Paths.get("template.html")), StandardCharsets.UTF_8)
                            .replace("__CHAPTERS__", chaptersJson());
                    send(ex, 200, "text/html; charset=utf-8", page.getBytes(StandardCharsets.UTF_8));
                } else if (path.equals("/cover.jpg")) {
                    send(ex, 200, "image/jpeg", Files.readAllBytes(Paths.get("cover.jpg")));
                } else {
                    send(ex, 404, "text/plain; charset=utf-8", "Not found".getBytes(StandardCharsets.UTF_8));
                }
            } catch (Exception e) {
                send(ex, 500, "text/plain; charset=utf-8", ("Error: " + e.getMessage()).getBytes(StandardCharsets.UTF_8));
            }
        });
        server.start();
        System.out.println("Book site running at http://localhost:" + port + "  (Ctrl+C to stop)");
    }
}
