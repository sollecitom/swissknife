dependencies {
    api(libs.apache.avro.core)
    api(projects.serializationDomain)

    implementation(projects.resourceUtils)

    testImplementation(projects.coreTestUtils)
}