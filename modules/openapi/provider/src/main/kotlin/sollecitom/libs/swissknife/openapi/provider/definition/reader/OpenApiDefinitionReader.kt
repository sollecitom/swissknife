package sollecitom.libs.swissknife.openapi.provider.definition.reader

import sollecitom.libs.swissknife.openapi.provider.definition.OpenApiDefinition

fun interface OpenApiDefinitionReader {

    fun read(openApiLocation: String): OpenApiDefinition

    companion object
}