package com.gatehill.vertxoas.service

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.PathItem
import io.swagger.v3.oas.models.Paths
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.parameters.Parameter
import io.swagger.v3.oas.models.servers.Server
import io.swagger.v3.oas.models.tags.Tag
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.Router
import io.vertx.ext.web.impl.RouteImpl
import org.apache.logging.log4j.LogManager
import java.util.Objects.nonNull

/**
 * @author Pete Cornish &lt;outofcoffee@gmail.com&gt;
 */
class OpenApiServiceImpl : OpenApiService {
    override fun buildSpecification(router: Router, basePath: String, title: String?): OpenAPI {
        LOGGER.debug("Generating specification for ${router.routes.size} route(s)")

        val spec = OpenAPI()

        val info = Info()
        spec.info = info

        val servers = mutableListOf<Server>()
        spec.servers = servers

        val paths = Paths()
        spec.paths = paths

        val tags = mutableListOf<Tag>()
        spec.tags = tags

        val description = StringBuilder()
                .append("This specification was generated from a Vert.x Web Router.")

        router.routes.filter { nonNull(it.path) }.forEach { route ->
            val splitPath = route.path.split("/")

            val convertedPath = splitPath.joinToString("/") {
                if (it.startsWith(":")) {
                    "{${it.substring(1)}}"
                } else {
                    it
                }
            }

            val pathItem = PathItem().apply {
                val operation = Operation().apply {
                    parameters = splitPath
                            .filter { it.startsWith(":") }
                            .map { it.substring(1) }
                            .map { param ->
                                Parameter().apply {
                                    name = param
                                    required = true
                                    allowEmptyValue = false
                                }
                            }
                }

                (route as RouteImpl).javaClass.getDeclaredField("methods").apply {
                    isAccessible = true

                    @Suppress("UNCHECKED_CAST")
                    val methods = get(route) as Collection<HttpMethod>

                    for (method in methods) {
                        when (method) {
                            HttpMethod.OPTIONS -> options = operation
                            HttpMethod.GET -> get = operation
                            HttpMethod.HEAD -> head = operation
                            HttpMethod.POST -> post = operation
                            HttpMethod.PUT -> put = operation
                            HttpMethod.DELETE -> delete = operation
                            HttpMethod.TRACE -> trace = operation
                            HttpMethod.PATCH -> patch = operation
                            HttpMethod.CONNECT -> TODO()
                            HttpMethod.OTHER -> TODO()
                        }
                    }
                }
            }

            paths.put(convertedPath, pathItem)
        }

        // info
        info.title = title ?: DEFAULT_TITLE
        info.description = description.toString()

        return spec
    }

    companion object {
        private val LOGGER = LogManager.getLogger(OpenApiServiceImpl::class.java)
        private val DEFAULT_TITLE = "Vert.x APIs"
    }
}
