dependencies {
    api(projects.swissknifeSqlPostgresUtils)
    api(projects.swissknifeTestContainersUtils)
    api(projects.swissknifeCoreDomain)
    api(libs.test.containers.postgres)

    runtimeOnly(libs.postgres)
}