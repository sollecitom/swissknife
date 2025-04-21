dependencies {
    implementation(projects.swissknifeLoggerCore)
    implementation(projects.swissknifeTestUtils)

    runtimeOnly(projects.swissknifeLoggerSlf4jAdapter)
}