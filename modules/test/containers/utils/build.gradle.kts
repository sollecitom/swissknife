plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(libs.test.containers.junit.jupiter)
    api(projects.testUtils)
}