package sollecitom.libs.swissknife.openapi.provider.definition.reader

import sollecitom.libs.swissknife.openapi.provider.definition.OpenApiDefinition
import io.swagger.v3.core.util.Json
import io.swagger.v3.core.util.Yaml
import io.swagger.v3.oas.models.OpenAPI

internal class ResolvedOpenApiDefinition(private val api: OpenAPI) : OpenApiDefinition {

    override val asYaml: String by lazy { Yaml.pretty().writeValueAsString(api) }
    override val asJson: String by lazy { Json.pretty().writeValueAsString(api).replace("\r", "") }
}