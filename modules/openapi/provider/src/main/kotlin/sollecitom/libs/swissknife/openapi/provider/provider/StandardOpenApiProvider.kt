package sollecitom.libs.swissknife.openapi.provider.provider

// TODO move this to Pillar
private object StandardOpenApiProvider : OpenApiProvider by LocationBasedOpenApiProvider(openApiFileName = "api/api.yaml")

val OpenApiProvider.Companion.standard: OpenApiProvider get() = StandardOpenApiProvider

val OpenApiLocationProvider.Companion.standard: OpenApiLocationProvider get() = StandardOpenApiProvider