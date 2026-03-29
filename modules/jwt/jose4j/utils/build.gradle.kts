plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(libs.jose4j)
    api(projects.jwtDomain)
    api(projects.kotlinExtensions)
}