package sollecitom.libs.swissknife.readiness.domain

class ClientAndCheck<out CLIENT : Any>(val value: CLIENT, readinessCheck: ReadinessAware): ReadinessAware by readinessCheck