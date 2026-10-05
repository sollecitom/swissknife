package sollecitom.libs.swissknife.kotlin.extensions.flows

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.onSuccess
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.selects.select
import kotlin.time.Duration

/** Batches flow emissions into lists of at most [maxChunkSize] elements. */
fun <T> Flow<T>.chunk(maxChunkSize: Int): Flow<List<T>> = chunkUntil(aggregateUntil = { _, rawChunk -> rawChunk.size >= maxChunkSize })

/** Batches flow emissions while the predicate returns true; flushes the chunk when it returns false. */
fun <T> Flow<T>.chunkWhile(aggregateWhile: (index: Int, rawChunk: List<T>) -> Boolean): Flow<List<T>> = chunkUntil(aggregateUntil = aggregateWhile.toUntilPredicate())

/** Batches flow emissions until the predicate returns true, then flushes the chunk. */
fun <T> Flow<T>.chunkUntil(aggregateUntil: (index: Int, rawChunk: List<T>) -> Boolean): Flow<List<T>> = chunkUntilPrivate(maxChunkingPeriod = null, aggregateUntil = aggregateUntil)

/** Batches flow emissions by size or time, whichever comes first. */
fun <T> Flow<T>.chunk(maxChunkSize: Int, maxChunkingPeriod: Duration): Flow<List<T>> = chunkUntil(aggregateUntil = { _, rawChunk -> rawChunk.size >= maxChunkSize }, maxChunkingPeriod = maxChunkingPeriod)

/** Batches flow emissions by time, flushing every [maxChunkingPeriod]. */
fun <T> Flow<T>.chunk(maxChunkingPeriod: Duration): Flow<List<T>> = chunkUntilPrivate(aggregateUntil = null, maxChunkingPeriod = maxChunkingPeriod)

fun <T> Flow<T>.chunkWhile(aggregateWhile: (index: Int, rawChunk: List<T>) -> Boolean, maxChunkingPeriod: Duration): Flow<List<T>> = chunkUntil(aggregateUntil = aggregateWhile.toUntilPredicate(), maxChunkingPeriod = maxChunkingPeriod)

fun <T> Flow<T>.chunkUntil(aggregateUntil: (index: Int, rawChunk: List<T>) -> Boolean, maxChunkingPeriod: Duration): Flow<List<T>> = chunkUntilPrivate(aggregateUntil = aggregateUntil, maxChunkingPeriod = maxChunkingPeriod)

private fun <T> Flow<T>.chunkUntilPrivate(maxChunkingPeriod: Duration?, aggregateUntil: ((index: Int, rawChunk: List<T>) -> Boolean)?): Flow<List<T>> = flow {

    coroutineScope {
        val values = buffer(Channel.RENDEZVOUS).produceIn(this)
        val periodEnds = maxChunkingPeriod?.let { period -> produce { while (true) { delay(period); send(Unit) } } }
        val chunk = mutableListOf<T>()
        var index = 0
        var isUpstreamComplete = false
        while (!isUpstreamComplete) {
            select {
                values.onReceiveCatching { received ->
                    received.exceptionOrNull()?.let { throw it }
                    received.onSuccess { value ->
                        if (aggregateUntil != null && aggregateUntil(index, chunk)) {
                            emitAndClear(chunk)
                        }
                        chunk += value
                        index++
                    }
                    isUpstreamComplete = received.isClosed
                }
                if (periodEnds != null) {
                    periodEnds.onReceive { emitAndClear(chunk) }
                }
            }
        }
        periodEnds?.cancel()
        emitAndClear(chunk)
    }
}

private suspend fun <T> FlowCollector<List<T>>.emitAndClear(chunk: MutableList<T>) {
    emit(chunk.toList())
    chunk.clear()
}

private fun <T> ((Int, List<T>) -> Boolean).toUntilPredicate(): ((Int, List<T>) -> Boolean) = { index, rawChunk -> !invoke(index, rawChunk) }