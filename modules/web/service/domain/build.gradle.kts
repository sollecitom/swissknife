dependencies {
    api(projects.coreDomain)
    api(projects.dddDomain)

    implementation(projects.kotlinExtensions)

    testImplementation(projects.testUtils)
}
