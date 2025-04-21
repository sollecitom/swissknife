dependencies {
    api(projects.loggerCore)
    api(projects.loggerJsonFormatter)
    api(projects.configurationUtils)

    testImplementation(projects.testUtils)
    testImplementation(projects.jsonTestUtils)
    testRuntimeOnly(projects.loggerSlf4jAdapter)
}