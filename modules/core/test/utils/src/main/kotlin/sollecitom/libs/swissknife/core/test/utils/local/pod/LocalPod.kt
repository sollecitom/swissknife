package sollecitom.libs.swissknife.core.test.utils.local.pod

import sollecitom.libs.swissknife.core.domain.lifecycle.Startable
import sollecitom.libs.swissknife.core.domain.lifecycle.Stoppable

interface LocalPod : Startable, Stoppable {

    val size: Int
    val logs: Logs

    interface Logs {

        fun pretty(): String

        fun byLabel(): Map<String, String>

        fun forInstanceWithLabel(label: String): String
    }

    companion object
}