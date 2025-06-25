dependencies {
    api(projects.kotlinExtensions)
    implementation(libs.ulid.creator)
    implementation(libs.ksuid.creator)
    implementation(libs.guava)
    implementation(projects.loggerCore)

    testImplementation(projects.testUtils)
}