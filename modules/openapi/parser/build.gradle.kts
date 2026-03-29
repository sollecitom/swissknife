plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(libs.swagger.parser)

    implementation(projects.kotlinExtensions)

    testImplementation(projects.resourceUtils)
    testImplementation(projects.testUtils)
}