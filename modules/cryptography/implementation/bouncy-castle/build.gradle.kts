dependencies {
    api(projects.swissknifeCryptographyDomain)

    implementation(libs.bcprov)
    implementation(libs.bcpkix)
    implementation(libs.bcutil)
    implementation(projects.swissknifeHashingUtils)
    implementation(projects.swissknifeKotlinExtensions)

    testImplementation(projects.swissknifeCryptographyTestSpecification)
}