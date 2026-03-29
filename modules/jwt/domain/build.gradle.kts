plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(projects.coreDomain)
    api(libs.org.json)

    implementation(projects.kotlinExtensions)

    testImplementation(projects.testUtils)
}