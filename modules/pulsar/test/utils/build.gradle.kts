plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(projects.pulsarUtils)
    api(libs.test.containers.pulsar)
    api(projects.coreTestUtils)
    api(projects.testContainersUtils)
}