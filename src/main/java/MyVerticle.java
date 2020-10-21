import io.vertx.core.*;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class MyVerticle extends AbstractVerticle {

    WebClient client;
    final static int NUM_OF_REQUESTS = 100;

    // Start when deploy
    public void start(Promise<Void> promise) {

        client = WebClient.create(vertx);

        List<Future> futures=new ArrayList<>();

        IntStream stream = IntStream.range(0, NUM_OF_REQUESTS);
        stream.forEach( id -> futures.add(send(id)));

        CompositeFuture.all(futures).onComplete(ar ->{
           System.out.println("sync end");
        });

    }

    public Future<Void> send(int id) {

        System.out.println("Send request id: " + id);

        Promise<Void> promise = Promise.promise();

        client.post(8081, "localhost", "/")

                .sendJsonObject(new JsonObject().put("anyId", id), ar -> {
                    if (ar.succeeded()) {
                        System.out.println("response accepted: " + ar.result().bodyAsString());
                    }else {
                        System.out.println("I hate vertx: " + ar.toString());
                    }
                    promise.complete();
                });

        return promise.future();
    }


    public static void main(String[]args){
        Vertx vertx = Vertx.vertx();

        Verticle server = new Server();
        vertx.deployVerticle(server);

        Verticle myVerticle = new MyVerticle();
        vertx.deployVerticle(myVerticle);
     }
    }


