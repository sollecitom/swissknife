plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(projects.coreDomain)

    testImplementation(projects.testUtils)
}