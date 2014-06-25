import org.vertx.java.core.Handler;
import org.vertx.java.core.VoidHandler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.ServerWebSocket;
import org.vertx.java.core.net.NetSocket;
import org.vertx.java.platform.Verticle;

import java.util.Set;

/**
 * Created by Administrator on 2014-06-23.
 */
public class DataFeeder extends Verticle {
    @Override
    public void start() {
        HttpServer httpServer = vertx.createHttpServer();
        httpServer.requestHandler(new Handler<HttpServerRequest>() {
            @Override
            public void handle(HttpServerRequest req) {
                if (req.path().equals("/")) {
                    req.response().sendFile("df.html");
                }
            }
        });
        httpServer.websocketHandler(new Handler<ServerWebSocket>() {
            @Override
            public void handle(final ServerWebSocket ws) {
                if (ws.path().equals("/feeder")) {
                    ws.dataHandler(new Handler<Buffer>() {
                        @Override
                        public void handle(Buffer buffer) {
                            Set<String> actorIDs = vertx.sharedData().getSet("conns");
                            for (String actorID : actorIDs) {
                                vertx.eventBus().publish(actorID, buffer.toString());
                            }
                        }
                    });
                } else if (ws.path().equals("/chart")) {
                    Set<String> set = vertx.sharedData().getSet("conns");
                    set.add(ws.textHandlerID());
                    ws.closeHandler(new VoidHandler() {
                        @Override
                        protected void handle() {
                            vertx.sharedData().getSet("conns").remove(ws.textHandlerID());
                        }
                    });
                }
            }
        });
        httpServer.listen(8080);

    }
}
