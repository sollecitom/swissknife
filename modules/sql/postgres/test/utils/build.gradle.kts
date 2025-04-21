dependencies {
    api(projects.swissknifeSqlPostgresContainer)
    api(projects.swissknifeTestUtils)
    api(projects.swissknifeSqlMigratorDomain)
    api(projects.swissknifeSqlMigratorLiquibase)

    runtimeOnly(libs.postgres)
}