dependencies {
    api(projects.jsonUtils)
    api(projects.pulsarUtils)

    implementation(projects.loggerCore)

    testImplementation(projects.coreTestUtils)
}