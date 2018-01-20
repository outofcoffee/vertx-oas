package com.gatehill.vertxoas

import com.gatehill.vertxoas.service.OpenApiServiceImpl
import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import org.amshove.kluent.`should be empty`
import org.amshove.kluent.`should be false`
import org.amshove.kluent.`should be true`
import org.amshove.kluent.`should equal`
import org.amshove.kluent.`should not be null`
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

/**
 * Specification for `OpenApiService`.
 *
 * @author pete
 */
object OpenApiServiceSpec : Spek({
    describe("an Open API service") {
        val service = OpenApiServiceImpl()

        on("generate") {
            val router = Router.router(Vertx.vertx()).apply {
                post("/users").handler({})
                get("/users/:userId").handler({})
            }

            val spec = service.buildSpecification(router, "/")

            it("converts routes to paths") {
                spec.paths.`should not be null`()
                spec.paths.size `should equal` 2
            }

            it("maps POST operation") {
                val postPaths = spec.paths.filterValues { it.post != null }
                postPaths.size `should equal` 1

                val params = postPaths.values.first().post.parameters
                params.`should not be null`()
                params.`should be empty`()
            }

            it("maps GET operation") {
                val getPaths = spec.paths.filterValues { it.get != null }
                getPaths.size `should equal` 1

                val params = getPaths.values.first().get.parameters
                params.`should not be null`()
                params.size `should equal` 1

                params.first().name `should equal` "userId"
                params.first().required.`should be true`()
                params.first().allowEmptyValue.`should be false`()
            }
        }
    }
})
