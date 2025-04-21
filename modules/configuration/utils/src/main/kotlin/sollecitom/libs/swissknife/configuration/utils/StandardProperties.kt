package sollecitom.libs.swissknife.configuration.utils

import sollecitom.libs.swissknife.lens.core.extensions.naming.name
import org.http4k.config.EnvironmentKey
import org.http4k.lens.long
import kotlin.random.Random

private val randomSeedKey = EnvironmentKey.long().optional("random.seed", "The seed (long) used to initialise random data generation")
val EnvironmentKey.randomSeed get() = randomSeedKey

private val instanceNodeNameKey = EnvironmentKey.name().required("instance.node.name", "The service node name in the cluster. Must be unique. Used to avoid re-assigning Pulsar partitions due to short restarts of services with consumers.")
val EnvironmentKey.instanceNodeName get() = instanceNodeNameKey

private val instanceGroupNameKey = EnvironmentKey.name().required("instance.group.name", "The name of the instance group name. Basically, the service name.")
val EnvironmentKey.instanceGroupName get() = instanceGroupNameKey