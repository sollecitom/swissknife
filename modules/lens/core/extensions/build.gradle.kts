dependencies {
    api(platform(libs.http4k.bom))
    api(libs.http4k.platform.k8s)
    api(libs.http4k.core)
    api(projects.coreDomain)

    implementation(projects.kotlinExtensions)

    testImplementation(projects.testUtils)
}