dependencies {
    api(projects.swissknifeCoreTestUtils)
    api(projects.swissknifeDddTestUtils)
    api(projects.swissknifeTestUtils)
    api(projects.swissknifeJsonTestUtils)
    api(projects.swissknifeJwtJose4jUtils)

    implementation(projects.swissknifeJwtJose4jIssuer)
    implementation(projects.swissknifeJwtJose4jProcessor)
}