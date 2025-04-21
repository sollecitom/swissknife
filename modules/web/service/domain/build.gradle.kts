dependencies {
    api(projects.swissknifeCoreDomain)
    api(projects.swissknifeDddDomain)

    implementation(projects.swissknifeKotlinExtensions)

    testImplementation(projects.swissknifeTestUtils)
}
