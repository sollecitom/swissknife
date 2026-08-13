package sollecitom.libs.swissknife.cryptography.implementation.bouncycastle

import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security

internal const val BC_PROVIDER = BouncyCastleProvider.PROVIDER_NAME

/**
 * Registers the Bouncy Castle provider unless it is already installed.
 *
 * Idempotent, so every entry point that reaches the JCE can call it without coordinating with the others.
 */
internal fun ensureBouncyCastleProviderIsRegistered() {
    if (Security.getProvider(BC_PROVIDER) == null) {
        Security.addProvider(BouncyCastleProvider())
    }
}
