plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(projects.openapiValidationRequestValidator)
    api(projects.testUtils)

    implementation(projects.kotlinExtensions)
}