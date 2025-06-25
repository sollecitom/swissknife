dependencies {
    api(projects.openapiValidationRequestValidator)
    api(projects.http4kUtils)

    implementation(projects.kotlinExtensions)
    implementation(projects.jsonUtils)
    implementation(projects.loggerCore)

    testImplementation(projects.openapiValidationRequestValidatorTestUtils)
    testImplementation(projects.openapiBuilder)
    testImplementation(projects.kotlinExtensions)
    testImplementation(projects.resourceUtils)
    testImplementation(projects.testUtils)
    testImplementation(projects.loggingStandardSlf4jConfiguration)
}