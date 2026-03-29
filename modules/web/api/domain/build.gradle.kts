plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(platform(libs.http4k.bom))
    api(projects.http4kServerUtils)

    testImplementation(projects.testUtils)
}
