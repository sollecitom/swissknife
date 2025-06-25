dependencies {
    api(projects.avroSerializationUtils)
    api(projects.coreDomain)

    implementation(projects.resourceUtils)
    implementation(libs.classgraph)

    testImplementation(projects.coreTestUtils)
}