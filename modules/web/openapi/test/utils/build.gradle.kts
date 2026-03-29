plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(projects.webApiUtils)
    api(projects.webApiTestUtils)
    implementation(projects.openapiParser)
}
