package sollecitom.libs.swissknife.hashing.utils.murmur3

import sollecitom.libs.swissknife.hashing.utils.Hash128Result
import sollecitom.libs.swissknife.hashing.utils.HashFunction
import sollecitom.libs.swissknife.hashing.utils.LongHashFunctionAdapter
import sollecitom.libs.swissknife.hashing.utils.LongTupleHashFunctionAdapter
import net.openhft.hashing.LongHashFunction
import net.openhft.hashing.LongTupleHashFunction

object Murmur3 {

    val hash64: HashFunction<Long> by lazy { hash64(seed = 0L) }

    val hash128: HashFunction<Hash128Result> by lazy { hash128(seed = 0L) }

    fun hash64(seed: Long): HashFunction<Long> = LongHashFunctionAdapter(delegate = murmur3_64(seed = seed))

    fun hash128(seed: Long): HashFunction<Hash128Result> = LongTupleHashFunctionAdapter(delegate = murmur3_128(seed = seed))

    private fun murmur3_64(seed: Long): LongHashFunction = if (seed == 0L) LongHashFunction.murmur_3() else LongHashFunction.murmur_3(seed)
    private fun murmur3_128(seed: Long): LongTupleHashFunction = if (seed == 0L) LongTupleHashFunction.murmur_3() else LongTupleHashFunction.murmur_3(seed)
}