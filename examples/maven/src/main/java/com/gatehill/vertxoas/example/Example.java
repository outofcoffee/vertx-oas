package com.gatehill.vertxoas.example;

import com.gatehill.vertxoas.RouterSpecGenerator;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

/**
 * @author Pete Cornish {@literal <outofcoffee@gmail.com>}
 */
public class Example {
    public static void main(String[] args) {
        final Vertx vertx = Vertx.factory.vertx();

        // your normal Vert.x Web Router with paths etc.
        final Router router = Router.router(vertx);
        router.post("/users").handler(routingContext -> { /* etc... */ });
        router.get("/users/:userId").handler(routingContext -> { /* etc... */ });

        // publish the Swagger/OpenAPI specification to a URL
        RouterSpecGenerator.publishApiDocs(router, "/api/spec");

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(8080, result -> {
                    if (result.succeeded()) {
                        System.out.println("API specification served at: http://localhost:8080/api/spec");
                    } else {
                        result.cause().printStackTrace();
                    }
                });
    }
}
