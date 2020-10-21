
import io.vertx.core.AbstractVerticle;

public class Server extends AbstractVerticle {


    @Override
    public void start() throws Exception {

        vertx.createHttpServer().requestHandler(req -> {
            req.bodyHandler(bodyHandler ->{
                System.out.println( "accept request: " + bodyHandler.toString());
            });
            req.response().end("{\"status\": 200}");

        }).listen(8081, listenResult -> {

            if (listenResult.failed()) {
                System.out.println("Could not start HTTP server");
                listenResult.cause().printStackTrace();
            } else {
                System.out.println("Server started");
            }

        });
    }
}