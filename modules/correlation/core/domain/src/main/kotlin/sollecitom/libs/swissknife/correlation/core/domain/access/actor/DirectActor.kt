package sollecitom.libs.swissknife.correlation.core.domain.access.actor

import sollecitom.libs.swissknife.correlation.core.domain.access.authentication.Authentication

/** An actor performing actions directly on their own behalf. */
data class DirectActor(override val account: Actor.Account, override val authentication: Authentication) : Actor {

    override val benefitingAccount: Actor.Account
        get() = account

    companion object
}