dependencies {
    api(projects.swissknifeComplianceCheckerDomain)
    api(libs.apache.avro.core)

    testImplementation(projects.swissknifeTestUtils)
}