dependencies {
    api(projects.sqlReactiveUtils)
    api(libs.r2dbc.postgres)
    api(projects.readinessDomain)

    implementation(projects.loggerCore)
}