plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(projects.openapiParser)
    api(projects.coreDomain)

    implementation(projects.resourceUtils)
    implementation(projects.loggerCore)
}