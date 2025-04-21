dependencies {
    api(libs.apache.avro.core)
    api(projects.swissknifeSerializationDomain)

    implementation(projects.swissknifeResourceUtils)

    testImplementation(projects.swissknifeCoreTestUtils)
}