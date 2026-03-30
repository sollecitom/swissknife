package sollecitom.libs.swissknife.kotlin.extensions.concurrency

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors

private val virtualThreadExecutor = Executors.newVirtualThreadPerTaskExecutor()
private val virtualThreadDispatcher = virtualThreadExecutor.asCoroutineDispatcher()

/** A coroutine dispatcher backed by JVM virtual threads (Project Loom). */
val Dispatchers.VirtualThreads: CoroutineDispatcher get() = virtualThreadDispatcher