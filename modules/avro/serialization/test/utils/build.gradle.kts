plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(projects.avroSerializationUtils)
    api(projects.coreTestUtils)
    api(projects.complianceCheckerTestUtils)
}