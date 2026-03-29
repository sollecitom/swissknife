plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(projects.openapiValidationHttp4kValidator)
    api(projects.openapiValidationRequestValidatorTestUtils)
    api(projects.testUtils)

    implementation(projects.kotlinExtensions)
}