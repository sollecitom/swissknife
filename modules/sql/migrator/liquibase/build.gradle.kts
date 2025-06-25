dependencies {
    api(projects.sqlDomain)
    api(projects.sqlMigratorDomain)
    api(libs.liquibase)

    implementation(projects.loggerCore)

    runtimeOnly(libs.liquibase.slf4j)
    runtimeOnly(libs.snakeyaml)
}