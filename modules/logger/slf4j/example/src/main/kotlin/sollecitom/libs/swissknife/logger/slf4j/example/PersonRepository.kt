package sollecitom.libs.swissknife.logger.slf4j.example

interface PersonRepository {

    fun findById(id: String): Person?
}
