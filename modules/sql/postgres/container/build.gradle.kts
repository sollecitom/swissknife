dependencies {
    api(projects.sqlPostgresUtils)
    api(projects.testContainersUtils)
    api(projects.coreDomain)
    api(libs.test.containers.postgres)

    runtimeOnly(libs.postgres)
}