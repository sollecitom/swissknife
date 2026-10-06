package sollecitom.libs.swissknife.pulsar.utils

/** The names of the settings Pulsar's `ClientBuilder.loadConf` accepts. A test checks them against the Pulsar version in use. */
object PulsarClientSettings {

    val names: Set<String> = setOf(
        "serviceUrl", "serviceUrlQuarantineInitDurationMs", "serviceUrlQuarantineMaxDurationMs", "authPluginClassName", "authParams", "authParamMap", "originalPrincipal",
        "operationTimeoutMs", "lookupTimeoutMs", "statsIntervalSeconds", "numIoThreads", "numListenerThreads", "connectionsPerBroker", "connectionMaxIdleSeconds", "useTcpNoDelay",
        "useTls", "tlsKeyFilePath", "tlsCertificateFilePath", "tlsTrustCertsFilePath", "tlsAllowInsecureConnection", "tlsHostnameVerificationEnable", "tlsFactoryClassName", "tlsFactoryConfig",
        "concurrentLookupRequest", "maxLookupRequest", "maxLookupRedirects", "maxNumberOfRejectedRequestPerConnection", "keepAliveIntervalSeconds", "connectionTimeoutMs", "requestTimeoutMs",
        "readTimeoutMs", "autoCertRefreshSeconds", "initialBackoffIntervalNanos", "maxBackoffIntervalNanos", "enableBusyWait", "listenerName", "useKeyStoreTls", "sslProvider", "jsseProvider",
        "jcaProvider", "tlsKeyStoreType", "tlsKeyStorePath", "tlsKeyStorePassword", "tlsTrustStoreType", "tlsTrustStorePath", "tlsTrustStorePassword", "tlsCiphers", "tlsProtocols",
        "memoryLimitBytes", "proxyServiceUrl", "proxyProtocol", "enableTransaction", "dnsLookupBindAddress", "dnsLookupBindPort", "dnsServerAddresses", "socks5ProxyAddress",
        "socks5ProxyUsername", "socks5ProxyPassword", "socks5ProxyScope", "description", "lookupProperties", "tracingEnabled",
    )
}
