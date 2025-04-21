package sollecitom.libs.swissknife.service.domain

import sollecitom.libs.swissknife.core.domain.lifecycle.Startable
import sollecitom.libs.swissknife.core.domain.lifecycle.Stoppable
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.web.api.domain.endpoint.Endpoint

interface ServiceModule : Startable, Stoppable {

    val name: Name

    companion object
}

// TODO move to another module, removing the HTTP4K dependency from here
interface ServiceModuleWithHttpDrivingAdapter : ServiceModule {

    val httpEndpoints: Set<Endpoint>
}