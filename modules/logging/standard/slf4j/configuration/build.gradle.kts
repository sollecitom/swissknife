dependencies {
    api(projects.loggingStandardConfiguration)
    runtimeOnly(projects.loggerSlf4jAdapter)

    testImplementation(projects.testUtils)
    testImplementation(projects.jsonTestUtils)
}