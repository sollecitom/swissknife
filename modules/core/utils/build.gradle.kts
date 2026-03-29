plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(projects.coreDomain)
    api(projects.configurationUtils)

    implementation(projects.loggerCore)

    testImplementation(projects.testUtils)
}