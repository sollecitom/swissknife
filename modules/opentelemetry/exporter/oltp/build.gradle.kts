dependencies {
    api(platform(libs.opentelemetry.bom))
    api(projects.swissknifeOpentelemetryCore)
    api(libs.opentelemetry.exporter.otlp)
}