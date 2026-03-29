plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    implementation(projects.kotlinExtensions)

    testImplementation(projects.testUtils)
}