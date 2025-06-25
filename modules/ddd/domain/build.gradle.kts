dependencies {
    api(projects.coreDomain)
    api(projects.correlationCoreDomain)
    api(projects.kotlinExtensions)
    api(projects.coreUtils)
    api(projects.loggerCore)

    testImplementation(projects.testUtils)
}