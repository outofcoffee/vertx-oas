package com.gatehill.vertxoas

import com.gatehill.vertxoas.service.OpenApiServiceImpl
import io.swagger.util.Json
import io.swagger.util.Yaml
import io.vertx.ext.web.Router

object RouterSpecGenerator {
    private const val contentTypeHeader = "Content-Type"
    private val openApiService by lazy { OpenApiServiceImpl() }

    @JvmStatic
    fun publishApiDocs(router: Router, path: String) {
        val spec = openApiService.buildSpecification(router, "/")
        val jsonSpec by lazy { Json.pretty(spec) }
        val yamlSpec by lazy { Yaml.pretty().writeValueAsString(spec) }

        router.get(path + ".json").handler { routingContext ->
            routingContext.response()
                    .putHeader(contentTypeHeader, "application/json")
                    .end(jsonSpec)
        }
        router.get(path + ".yaml").handler { routingContext ->
            routingContext.response()
                    .putHeader(contentTypeHeader, "application/x-yaml")
                    .end(yamlSpec)
        }

        // default redirect
        router.get(path).handler { routingContext ->
            routingContext.response()
                    .putHeader("Location", "$path.yaml")
                    .setStatusCode(302)
                    .end()
        }
    }
}
