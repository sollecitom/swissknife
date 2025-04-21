dependencies {
    api(projects.swissknifeLoggingStandardConfiguration)
    runtimeOnly(projects.swissknifeLoggerSlf4jAdapter)

    testImplementation(projects.swissknifeTestUtils)
    testImplementation(projects.swissknifeJsonTestUtils)
}