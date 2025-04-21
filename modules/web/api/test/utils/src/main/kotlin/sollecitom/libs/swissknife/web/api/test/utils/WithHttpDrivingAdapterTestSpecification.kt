package sollecitom.libs.swissknife.web.api.test.utils

import sollecitom.libs.swissknife.web.service.domain.WithWebInterface

interface WithHttpDrivingAdapterTestSpecification : HttpDrivenTestSpecification {

    val service: WithWebInterface
}