plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(projects.coreDomain)
    api(projects.coreUtils)
    api(projects.webClientInfoDomain)

    implementation(projects.kotlinExtensions)
}