dependencies {
    api(projects.swissknifeLoggerCore)
    implementation(projects.swissknifeJsonUtils)
    implementation(projects.swissknifeResourceUtils)

    testImplementation(projects.swissknifeTestUtils)
    testImplementation(projects.swissknifeJsonTestUtils)
}