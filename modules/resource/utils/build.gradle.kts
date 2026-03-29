plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    implementation(libs.guava)

    testImplementation(projects.testUtils)
}