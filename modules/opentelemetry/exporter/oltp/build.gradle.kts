plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(platform(libs.opentelemetry.bom))
    api(projects.opentelemetryCore)
    api(libs.opentelemetry.exporter.otlp)
}