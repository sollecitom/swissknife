dependencies {
    api(projects.swissknifeSqlReactiveUtils)
    api(libs.r2dbc.postgres)
    api(projects.swissknifeReadinessDomain)

    implementation(projects.swissknifeLoggerCore)
}