package sollecitom.libs.swissknife.openapi.provider.provider

import io.swagger.v3.oas.models.OpenAPI

interface OpenApiProvider : OpenApiLocationProvider {

    val rawOpenApi: String
    val openApi: OpenAPI

    companion object
}