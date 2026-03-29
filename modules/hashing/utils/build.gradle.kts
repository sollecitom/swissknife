plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    implementation(projects.kotlinExtensions)
    implementation(libs.zero.allocation.hashing)
    implementation(libs.commons.codec)

    testImplementation(projects.testUtils)
}