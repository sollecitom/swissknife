dependencies {
    api(libs.org.json)
    api(projects.kotlinExtensions)
    api(projects.serializationDomain)
    api(projects.complianceCheckerDomain)

    implementation(libs.json.schema.parser) {
        exclude(group = "commons-collections", module = "commons-collections")
    }
    implementation(projects.resourceUtils)

    testImplementation(projects.testUtils)
}