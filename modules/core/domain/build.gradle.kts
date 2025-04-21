dependencies {
    api(projects.swissknifeKotlinExtensions)
    implementation(libs.ulid.creator)
    implementation(libs.ksuid.creator)
    implementation(libs.guava)
    implementation(projects.swissknifeLoggerCore)

    testImplementation(projects.swissknifeTestUtils)
}