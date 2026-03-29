plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(projects.complianceCheckerDomain)
    api(libs.apache.avro.core)

    testImplementation(projects.testUtils)
}