dependencies {
    api(projects.sqlDomain)
    api(libs.r2dbc.spi)
    api(libs.r2dbc.pool)
    api(libs.spring.data.r2dbc)
    api(projects.coreDomain)

    implementation(libs.kotlinx.coroutines.reactor)

    implementation(projects.kotlinExtensions)
}