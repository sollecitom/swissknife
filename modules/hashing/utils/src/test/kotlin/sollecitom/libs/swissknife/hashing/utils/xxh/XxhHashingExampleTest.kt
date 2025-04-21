package sollecitom.libs.swissknife.hashing.utils.xxh

import assertk.Assert
import assertk.assertions.isEqualTo
import sollecitom.libs.swissknife.hashing.utils.Hash128Result
import sollecitom.libs.swissknife.hashing.utils.HashFunctionTestSpecification
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class XxhHashingExampleTest {

    @Nested
    inner class Hash64 : HashFunctionTestSpecification<Long> {

        override fun hashFunction(seed: Long) = Xxh.hash64(seed)
        override val digest = "a message"
        override val expectedHash = 5785544599489281299L
    }

    @Nested
    inner class Hash128 : HashFunctionTestSpecification<Hash128Result> {

        override fun hashFunction(seed: Long) = Xxh.hash128(seed)
        override val digest = "a message"
        override val expectedHash = Hash128Result.create(3146448641567002184L, 8337944919793685661L)

        override fun Assert<Hash128Result>.matches(expected: Hash128Result) = given { actual ->

            assertThat(actual.firstHalf).isEqualTo(expected.firstHalf)
            assertThat(actual.secondHalf).isEqualTo(expected.secondHalf)
        }
    }
}