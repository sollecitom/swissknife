dependencies {
    api(projects.openapiParser)
    api(libs.swagger.request.validator) {
        exclude(group = "commons-codec", module = "commons-codec")
    }

    implementation(projects.kotlinExtensions)
}