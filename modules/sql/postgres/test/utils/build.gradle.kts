dependencies {
    api(projects.sqlPostgresContainer)
    api(projects.testUtils)
    api(projects.sqlMigratorDomain)
    api(projects.sqlMigratorLiquibase)

    runtimeOnly(libs.postgres)
}