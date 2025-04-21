package sollecitom.libs.swissknife.web.api.test.utils

import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.openapi.validation.http4k.test.utils.WithHttp4kOpenApiValidationSupport
import sollecitom.libs.swissknife.web.api.utils.api.HttpApiDefinition

interface EndpointTestSpecification : LocalHttpDrivingAdapterTestSpecification, CoreDataGenerator, WithHttp4kOpenApiValidationSupport, HttpApiDefinition {

    val version: String
    val pathWithoutVersion: String
    val path: String get() = pathWithVersion(version)

    fun pathWithVersion(version: String) = "$pathWithoutVersion/v$version"
}