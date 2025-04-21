dependencies {
    api(libs.org.json)
    api(projects.swissknifeKotlinExtensions)
    api(projects.swissknifeSerializationDomain)
    api(projects.swissknifeComplianceCheckerDomain)

    implementation(libs.json.schema.parser) {
        exclude(group = "commons-collections", module = "commons-collections")
    }
    implementation(projects.swissknifeResourceUtils)

    testImplementation(projects.swissknifeTestUtils)
}