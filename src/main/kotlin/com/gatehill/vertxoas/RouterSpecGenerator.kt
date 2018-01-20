package com.gatehill.vertxoas

import com.gatehill.vertxoas.service.OpenApiServiceImpl
import io.swagger.util.Json
import io.vertx.ext.web.Router

object RouterSpecGenerator {
    private val openApiService by lazy { OpenApiServiceImpl() }

    fun publishApiDocs(router: Router, path: String) {
        val spec = openApiService.buildSpecification(router, "/")
        val serialisedSpec = Json.pretty(spec)

        router.get(path).handler { routingContext ->
            routingContext.response().end(serialisedSpec)
        }
    }
}
