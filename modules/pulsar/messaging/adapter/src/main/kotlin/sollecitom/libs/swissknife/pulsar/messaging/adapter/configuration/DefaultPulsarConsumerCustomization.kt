package sollecitom.libs.swissknife.pulsar.messaging.adapter.configuration

import org.apache.pulsar.client.api.ConsumerBuilder
import org.apache.pulsar.client.api.SubscriptionInitialPosition
import org.apache.pulsar.client.api.SubscriptionMode
import org.apache.pulsar.client.api.SubscriptionType

fun <VALUE> ConsumerBuilder<VALUE>.defaultConsumerCustomization(): ConsumerBuilder<VALUE> {

    return subscriptionInitialPosition(SubscriptionInitialPosition.Earliest)
        .subscriptionMode(SubscriptionMode.Durable)
        .subscriptionType(SubscriptionType.Failover)
        .autoUpdatePartitions(true)
        .startPaused(true)
}