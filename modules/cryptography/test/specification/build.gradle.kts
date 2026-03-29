plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(projects.cryptographyDomain)
    api(projects.testUtils)

    implementation(projects.kotlinExtensions)
}