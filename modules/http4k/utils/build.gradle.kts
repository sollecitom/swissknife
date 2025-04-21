dependencies {
    api(platform(libs.http4k.bom))
    api(libs.http4k.core)
    api(libs.http4k.format.core)
    api(projects.swissknifeJsonUtils)
    api(projects.swissknifeCoreDomain)

    implementation(projects.swissknifeKotlinExtensions)

    testImplementation(projects.swissknifeTestUtils)
}