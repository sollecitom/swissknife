package sollecitom.libs.swissknife.core.utils

/** Composite generator providing random values, time, and unique ID generation. Use [CoreDataGenerator.provider] to create. */
interface CoreDataGenerator : RandomGenerator, TimeGenerator, UniqueIdGenerator {

    companion object
}

