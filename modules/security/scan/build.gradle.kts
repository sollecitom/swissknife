plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(projects.testUtils)
    api(libs.test.containers.junit.jupiter)
    implementation(libs.org.json)
}
