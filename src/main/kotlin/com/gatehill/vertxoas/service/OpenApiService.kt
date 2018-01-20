package com.gatehill.vertxoas.service

import io.swagger.v3.oas.models.OpenAPI
import io.vertx.ext.web.Router

/**
 * @author Pete Cornish &lt;outofcoffee@gmail.com&gt;
 */
interface OpenApiService {
    fun buildSpecification(router: Router, basePath: String, title: String? = null): OpenAPI
}
