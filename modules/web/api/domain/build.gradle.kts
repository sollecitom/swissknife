dependencies {
    api(platform(libs.http4k.bom))
    api(projects.http4kServerUtils)

    testImplementation(projects.testUtils)
}
