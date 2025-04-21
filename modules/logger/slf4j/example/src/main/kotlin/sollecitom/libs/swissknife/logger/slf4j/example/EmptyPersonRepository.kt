package sollecitom.libs.swissknife.logger.slf4j.example

import sollecitom.libs.swissknife.logger.core.loggable.Loggable

class EmptyPersonRepository : PersonRepository {

    override fun findById(id: String): Person? {

        logger.trace { "Entered method `findById` with arguments: {`id`: \"${id}\"}" }
        if (id.isEmpty()) {
            logger.error { "Received illegal empty ID" }
            return null
        }
        logger.info { "Found no person for ID: $id" }
        return null
    }

    companion object : Loggable()
}