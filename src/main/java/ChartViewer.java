import org.vertx.java.core.Handler;
import org.vertx.java.core.VoidHandler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.ServerWebSocket;
import org.vertx.java.platform.Verticle;

import java.util.Set;

/**
 * Created by Administrator on 2014-06-24.
 */
public class ChartViewer extends Verticle {
    @Override
    public void start() {
        HttpServer httpServer = vertx.createHttpServer();
        httpServer.requestHandler(new Handler<HttpServerRequest>() {
            @Override
            public void handle(HttpServerRequest req) {
                if (req.path().equals("/")) {
                    req.response().sendFile("cv2.html");
                }
            }
        });
        httpServer.listen(8081);
    }
}
