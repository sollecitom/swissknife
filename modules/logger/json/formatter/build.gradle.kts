dependencies {
    api(projects.loggerCore)
    implementation(projects.jsonUtils)
    implementation(projects.resourceUtils)

    testImplementation(projects.testUtils)
    testImplementation(projects.jsonTestUtils)
}