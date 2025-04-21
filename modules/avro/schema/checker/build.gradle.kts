dependencies {
    api(projects.complianceCheckerDomain)
    api(libs.apache.avro.core)

    testImplementation(projects.testUtils)
}