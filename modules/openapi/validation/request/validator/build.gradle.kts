dependencies {
    api(projects.swissknifeOpenapiParser)
    api(libs.swagger.request.validator) {
        exclude(group = "commons-codec", module = "commons-codec")
    }

    implementation(projects.swissknifeKotlinExtensions)
}