dependencies {
    api(platform(libs.opentelemetry.bom))
    api(projects.opentelemetryCore)
    api(libs.opentelemetry.exporter.otlp)
}