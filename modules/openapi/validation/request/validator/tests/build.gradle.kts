plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    testImplementation(projects.openapiValidationRequestValidator)
    testImplementation(projects.openapiValidationRequestValidatorTestUtils)
    testImplementation(projects.openapiBuilder)
    testImplementation(projects.kotlinExtensions)
    testImplementation(projects.resourceUtils)
    testImplementation(projects.loggingStandardSlf4jConfiguration)
}