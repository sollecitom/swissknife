plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(libs.test.containers.kycloak)
    api(libs.keycloak.authz.client)
    api(projects.coreTestUtils)
    api(projects.testContainersUtils)
}