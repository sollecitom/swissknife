dependencies {
    api(platform(libs.http4k.bom))
    api(projects.http4kServerUtils)
    api(projects.readinessDomain)
    api(libs.http4k.platform.k8s)
    api(projects.serviceDomain)
}
