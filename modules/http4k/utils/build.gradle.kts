dependencies {
    api(platform(libs.http4k.bom))
    api(libs.http4k.core)
    api(libs.http4k.format.core)
    api(projects.jsonUtils)
    api(projects.coreDomain)

    implementation(projects.kotlinExtensions)

    testImplementation(projects.testUtils)
}