dependencies {
    implementation(projects.loggerCore)
    implementation(projects.testUtils)

    runtimeOnly(projects.loggerSlf4jAdapter)
}