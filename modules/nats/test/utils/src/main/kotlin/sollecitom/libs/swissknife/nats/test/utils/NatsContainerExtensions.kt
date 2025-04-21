package sollecitom.libs.swissknife.nats.test.utils

import sollecitom.libs.swissknife.nats.client.NatsConsumer
import sollecitom.libs.swissknife.nats.client.NatsPublisher
import sollecitom.libs.swissknife.nats.client.create
import sollecitom.libs.swissknife.nats.client.server
import io.nats.client.Options
import org.testcontainers.containers.Network

private const val NATS_NETWORK_ALIAS = "nats"

fun NatsContainer.newPublisher(customize: Options.Builder.() -> Unit = {}) = NatsPublisher.create(options = Options.builder().server(host = host, port = clientPort).build())

fun NatsContainer.newConsumer(subjects: Set<String>, customize: Options.Builder.() -> Unit = {}) = NatsConsumer.create(options = Options.builder().server(host = host, port = clientPort).build(), subjects)

fun NatsContainer.newConsumer(subject: String, customize: Options.Builder.() -> Unit = {}) = newConsumer(setOf(subject), customize)

fun NatsContainer.withNetworkAndAliases(network: Network, vararg aliases: String = arrayOf(NATS_NETWORK_ALIAS)) = withNetwork(network).withNetworkAliases(*aliases)

val NatsContainer.networkAlias: String get() = NATS_NETWORK_ALIAS