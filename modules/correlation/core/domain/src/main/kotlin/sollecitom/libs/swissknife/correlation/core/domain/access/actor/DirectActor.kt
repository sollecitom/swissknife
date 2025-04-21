package sollecitom.libs.swissknife.correlation.core.domain.access.actor

import sollecitom.libs.swissknife.correlation.core.domain.access.authentication.Authentication

data class DirectActor(override val account: Actor.Account, override val authentication: Authentication) : Actor {

    override val benefitingAccount: Actor.Account
        get() = account

    companion object
}