plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(projects.coreDomain)

    implementation(projects.kotlinExtensions)

    testImplementation(projects.coreTestUtils)
}