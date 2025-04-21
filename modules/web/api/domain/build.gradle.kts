dependencies {
    api(platform(libs.http4k.bom))
    api(projects.swissknifeHttp4kServerUtils)

    testImplementation(projects.swissknifeTestUtils)
}
