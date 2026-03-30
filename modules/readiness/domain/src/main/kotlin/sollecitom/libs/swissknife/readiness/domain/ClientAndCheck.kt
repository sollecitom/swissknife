package sollecitom.libs.swissknife.readiness.domain

/** Pairs a client instance with its readiness check, delegating [ReadinessAware] to the check. */
class ClientAndCheck<out CLIENT : Any>(val value: CLIENT, readinessCheck: ReadinessAware): ReadinessAware by readinessCheck