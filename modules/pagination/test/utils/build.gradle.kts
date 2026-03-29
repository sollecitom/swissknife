plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(projects.paginationDomain)
    api(projects.coreUtils)
    api(projects.coreTestUtils)
    api(projects.testUtils)
}