dependencies {
    api(platform(libs.http4k.bom))
    api(libs.http4k.platform.k8s)
    api(projects.coreDomain)
    api(projects.lensCoreExtensions)

    implementation(projects.kotlinExtensions)
    implementation(projects.resourceUtils)
    implementation(libs.http4k.format.jackson.yaml)

    testRuntimeOnly(libs.http4k.format.jackson.yaml)
    testImplementation(projects.testUtils)
}