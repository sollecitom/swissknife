package sollecitom.libs.swissknife.kotlin.extensions.flows

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
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

private fun <T> Flow<T>.chunkUntilPrivate(maxChunkingPeriod: Duration?, aggregateUntil: ((index: Int, rawChunk: List<T>) -> Boolean)?): Flow<List<T>> = object : Flow<List<T>> {

    private var original = this@chunkUntilPrivate

    override suspend fun collect(collector: FlowCollector<List<T>>) = coroutineScope {

        val chunk = mutableListOf<T>()

        suspend fun flush() {
            collector.emit(chunk)
            chunk.clear()
        }

        var ticker: Job? = null
        if (maxChunkingPeriod != null) {
            val periods = generateSequence(1) { it + 1 }.asFlow().onEach { delay(maxChunkingPeriod) }
            ticker = async(start = CoroutineStart.LAZY) { periods.onEach { flush() }.collect() }
        }

        original = original.onCompletion {
            ticker?.cancel()
            flush()
        }

        ticker?.start()
        original.collectIndexed { index, value ->
            if (aggregateUntil != null && aggregateUntil(index, chunk)) {
                flush()
            }
            chunk += value
        }
    }
}

private fun <T> ((Int, List<T>) -> Boolean).toUntilPredicate(): ((Int, List<T>) -> Boolean) = { index, rawChunk -> !invoke(index, rawChunk) }