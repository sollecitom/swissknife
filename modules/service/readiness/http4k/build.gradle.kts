dependencies {
    api(platform(libs.http4k.bom))
    api(projects.swissknifeHttp4kServerUtils)
    api(projects.swissknifeReadinessDomain)
    api(libs.http4k.platform.k8s)
    api(projects.swissknifeServiceDomain)
}
