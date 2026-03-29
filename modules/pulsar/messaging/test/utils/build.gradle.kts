plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(projects.pulsarMessagingAdapter)
    api(projects.coreTestUtils)
    api(projects.pulsarTestUtils)
    api(projects.testContainersUtils)
    api(projects.pulsarJsonSerialization)
    api(projects.pulsarAvroUtils)
    api(projects.pulsarJsonUtils)
}