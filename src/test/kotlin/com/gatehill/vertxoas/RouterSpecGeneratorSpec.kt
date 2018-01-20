package com.gatehill.vertxoas

import com.jayway.restassured.RestAssured
import com.jayway.restassured.RestAssured.given
import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import org.hamcrest.Matchers.endsWith
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.startsWith
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.net.ServerSocket
import java.util.concurrent.CountDownLatch

/**
 * Specification for `OpenApiService`.
 *
 * @author pete
 */
object RouterSpecGeneratorSpec : Spek({
    val port = ServerSocket(0).use { it.localPort }
    RestAssured.baseURI = "http://localhost:$port"

    val vertx = Vertx.vertx()
    val router = Router.router(vertx).apply {
        post("/users").handler({})
        get("/users/:userId").handler({})
    }

    val listenLatch = CountDownLatch(1)
    vertx.createHttpServer()
            .requestHandler(router::accept)
            .listen(port, { listenLatch.countDown() })
    listenLatch.await()

    describe("a Router specification generator") {
        on("processing the router") {
            RouterSpecGenerator.publishApiDocs(router, "/spec")

            it("redirects to the default specification") {
                given().redirects().follow(false)
                        .get("/spec")
                        .then()
                        .statusCode(equalTo(302))
                        .and()
                        .header("Location", endsWith(".yaml"))
                        .log().all()
            }

            it("exposes a JSON specification") {
                given().get("/spec.json")
                        .then()
                        .statusCode(equalTo(200))
                        .and()
                        .contentType(equalTo("application/json"))
                        .and()
                        .body("openapi", startsWith("3"))
                        .log().all()
            }

            it("exposes a YAML specification") {
                given().get("/spec.yaml")
                        .then()
                        .statusCode(equalTo(200))
                        .and()
                        .contentType(equalTo("application/x-yaml"))
                        .log().all()
            }
        }
    }

    afterGroup {
        vertx.close()
    }
})
