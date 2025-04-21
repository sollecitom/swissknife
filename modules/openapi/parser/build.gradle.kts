dependencies {
    api(libs.swagger.parser)

    implementation(projects.swissknifeKotlinExtensions)

    testImplementation(projects.swissknifeResourceUtils)
    testImplementation(projects.swissknifeTestUtils)
}