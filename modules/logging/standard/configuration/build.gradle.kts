dependencies {
    api(projects.swissknifeLoggerCore)
    api(projects.swissknifeLoggerJsonFormatter)
    api(projects.swissknifeConfigurationUtils)

    testImplementation(projects.swissknifeTestUtils)
    testImplementation(projects.swissknifeJsonTestUtils)
    testRuntimeOnly(projects.swissknifeLoggerSlf4jAdapter)
}