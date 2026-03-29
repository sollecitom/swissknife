plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(libs.apache.avro.core)
    api(projects.serializationDomain)

    implementation(projects.resourceUtils)

    testImplementation(projects.coreTestUtils)
}