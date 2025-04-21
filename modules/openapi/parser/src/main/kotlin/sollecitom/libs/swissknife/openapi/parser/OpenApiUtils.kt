package sollecitom.libs.swissknife.openapi.parser

import io.swagger.v3.core.util.Json
import io.swagger.v3.core.util.Yaml
import io.swagger.v3.oas.models.OpenAPI

fun OpenAPI.asJsonString(): String = Json.pretty().writeValueAsString(this).replace("\r", "")

fun OpenAPI.asYamlString(): String = Yaml.pretty().writeValueAsString(this)