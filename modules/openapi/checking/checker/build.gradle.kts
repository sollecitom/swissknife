plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(projects.openapiParser)
    api(projects.complianceCheckerDomain)

    implementation(projects.kotlinExtensions)
    implementation(projects.jsonUtils)
}