dependencies {
    api(projects.swissknifeAvroSerializationUtils)
    api(projects.swissknifeCoreDomain)

    implementation(projects.swissknifeResourceUtils)
    implementation(libs.classgraph)

    testImplementation(projects.swissknifeCoreTestUtils)
}