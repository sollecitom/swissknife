dependencies {
    api(platform(libs.http4k.bom))
    api(libs.http4k.platform.k8s)
    api(projects.swissknifeCoreDomain)
    api(projects.swissknifeLensCoreExtensions)

    implementation(projects.swissknifeKotlinExtensions)
    implementation(projects.swissknifeResourceUtils)
    implementation(libs.http4k.format.jackson.yaml)

    testRuntimeOnly(libs.http4k.format.jackson.yaml)
    testImplementation(projects.swissknifeTestUtils)
}