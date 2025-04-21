package sollecitom.libs.swissknife.correlation.core.domain.access.authentication

import sollecitom.libs.swissknife.correlation.core.domain.access.session.SimpleSession

data class CredentialsBasedAuthentication(override val token: Authentication.Token, override val session: SimpleSession) : TokenBasedAuthentication.SessionBased.ClientSide<SimpleSession> {

    companion object
}