plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(projects.coreTestUtils)
    api(projects.dddTestUtils)
    api(projects.testUtils)
    api(projects.jsonTestUtils)
    api(projects.jwtJose4jUtils)

    implementation(projects.jwtJose4jIssuer)
    implementation(projects.jwtJose4jProcessor)
}