dependencies {
    api(libs.swagger.parser)

    implementation(projects.kotlinExtensions)

    testImplementation(projects.resourceUtils)
    testImplementation(projects.testUtils)
}