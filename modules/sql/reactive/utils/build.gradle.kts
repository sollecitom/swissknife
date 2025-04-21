dependencies {
    api(projects.swissknifeSqlDomain)
    api(libs.r2dbc.spi)
    api(libs.r2dbc.pool)
    api(libs.spring.data.r2dbc)
    api(projects.swissknifeCoreDomain)

    implementation(libs.kotlinx.coroutines.reactor)

    implementation(projects.swissknifeKotlinExtensions)
}