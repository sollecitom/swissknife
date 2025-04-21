package sollecitom.libs.swissknife.test.containers.utils

import com.github.dockerjava.api.DockerClient
import org.testcontainers.DockerClientFactory
import org.testcontainers.containers.Container
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy

private val docker: DockerClient by lazy { DockerClientFactory.lazyClient() }

fun Container<*>.stopWithoutDeletingContainer() {
    docker.stopContainerCmd(containerId).exec()
}

fun Container<*>.startFromExistingStoppedContainer() {
    docker.startContainerCmd(containerId).exec()
}

fun Container<*>.restart() {
    stopWithoutDeletingContainer()
    startFromExistingStoppedContainer()
}

fun Container<*>.pause() {
    docker.pauseContainerCmd(containerId).exec()
}

fun Container<*>.unpause() {
    docker.unpauseContainerCmd(containerId).exec()
}

fun Map.Entry<String, String>.toJavaArgument(): String = "-D$key=$value"

fun Map<String, String>.toJavaArgumentsList(): List<String> = map { it.toJavaArgument() }

fun <CONTAINER : GenericContainer<CONTAINER>> CONTAINER.withJavaArgs(arguments: List<String>): CONTAINER = withEnv("JAVA_TOOL_OPTIONS", arguments.joinToString(separator = " "))

fun <CONTAINER : GenericContainer<CONTAINER>> CONTAINER.withJavaArgs(arguments: Map<String, String>): CONTAINER = withJavaArgs(arguments.toJavaArgumentsList())

fun <CONTAINER : GenericContainer<CONTAINER>> CONTAINER.waitingForSuccessfulReadinessCheck(healthPort: Int, readinessPath: String = "/readiness"): CONTAINER {

    return waitingFor(HttpWaitStrategy().forPort(healthPort).withMethod("GET").forPath(readinessPath).forStatusCode(200).allowInsecure())
}