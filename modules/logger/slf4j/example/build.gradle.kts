plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    implementation(projects.loggerCore)
    implementation(projects.testUtils)

    runtimeOnly(projects.loggerSlf4jAdapter)
}