package sollecitom.libs.swissknife.correlation.core.domain.access.authentication

import sollecitom.libs.swissknife.correlation.core.domain.access.session.Session

sealed interface TokenBasedAuthentication : Authentication {

    val token: Authentication.Token

    sealed interface SessionBased<SESSION : Session> : TokenBasedAuthentication {

        val session: SESSION

        sealed interface ClientSide<SESSION : Session> : SessionBased<SESSION> {

            companion object
        }

        companion object
    }

    companion object
}