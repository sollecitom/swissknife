dependencies {
    api(projects.dddDomain)
    api(projects.loggerCore)
    api(projects.kotlinExtensions)
    api(projects.correlationLoggingUtils)

    testImplementation(projects.testUtils)
}