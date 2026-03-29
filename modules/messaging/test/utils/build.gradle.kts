plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(projects.messagingDomain)
    api(projects.coreTestUtils)
    api(projects.dddTestUtils)
}