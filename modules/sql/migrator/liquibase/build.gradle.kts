dependencies {
    api(projects.swissknifeSqlDomain)
    api(projects.swissknifeSqlMigratorDomain)
    api(libs.liquibase)

    implementation(projects.swissknifeLoggerCore)

    runtimeOnly(libs.liquibase.slf4j)
    runtimeOnly(libs.snakeyaml)
}