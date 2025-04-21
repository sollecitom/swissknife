package sollecitom.libs.swissknife.openapi.checking.checker.model

import io.swagger.v3.oas.models.parameters.Parameter

data class ParameterWithLocation(val parameter: Parameter, val location: ParameterLocation) {

    val name: String get() = parameter.name
}