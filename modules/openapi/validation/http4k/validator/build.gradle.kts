dependencies {
    api(projects.swissknifeOpenapiValidationRequestValidator)
    api(projects.swissknifeHttp4kUtils)

    implementation(projects.swissknifeKotlinExtensions)
    implementation(projects.swissknifeJsonUtils)
    implementation(projects.swissknifeLoggerCore)

    testImplementation(projects.swissknifeOpenapiValidationRequestValidatorTestUtils)
    testImplementation(projects.swissknifeOpenapiBuilder)
    testImplementation(projects.swissknifeKotlinExtensions)
    testImplementation(projects.swissknifeResourceUtils)
    testImplementation(projects.swissknifeTestUtils)
    testImplementation(projects.swissknifeLoggingStandardSlf4jConfiguration)
}