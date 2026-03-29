plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(platform(libs.opentelemetry.bom))
    api(libs.opentelemetry.sdk.core)
    api(libs.opentelemetry.sdk.logs)
}