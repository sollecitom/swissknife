plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    testImplementation(projects.jwtDomain)
    testImplementation(projects.jwtTestUtils)
    testImplementation(projects.jwtJose4jProcessor)
    testImplementation(projects.jwtJose4jIssuer)
    testImplementation(projects.loggingStandardSlf4jConfiguration)
}