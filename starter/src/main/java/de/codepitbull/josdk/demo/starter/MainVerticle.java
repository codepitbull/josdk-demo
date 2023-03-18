package de.codepitbull.josdk.demo.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    vertx.createHttpServer().requestHandler(req -> {
      vertx.fileSystem()
        .readFile("/mapped")
        .andThen(res -> {
          if (res.failed()) {
            req.response()
              .putHeader("content-type", "text/plain")
              .end("Hello from Vert.x!");
          } else {
            req.response()
              .putHeader("content-type", "text/plain")
              .end(res.result());
          }
        });

    }).listen(8888, http -> {
      if (http.succeeded()) {
        startPromise.complete();
        System.out.println("HTTP server started on port 8888");
      } else {
        startPromise.fail(http.cause());
      }
    });
  }
}
