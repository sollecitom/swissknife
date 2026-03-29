plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(libs.slf4j.api)
    api(platform(libs.kotlinx.coroutines.bom))
    api(libs.kotlinx.coroutines.slf4j)

    implementation(libs.org.json)

    testImplementation(projects.testUtils)
}