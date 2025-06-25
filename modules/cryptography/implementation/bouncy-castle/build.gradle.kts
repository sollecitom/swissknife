dependencies {
    api(projects.cryptographyDomain)

    implementation(libs.bcprov)
    implementation(libs.bcpkix)
    implementation(libs.bcutil)
    implementation(projects.hashingUtils)
    implementation(projects.kotlinExtensions)

    testImplementation(projects.cryptographyTestSpecification)
}