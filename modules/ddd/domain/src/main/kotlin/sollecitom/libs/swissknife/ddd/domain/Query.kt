package sollecitom.libs.swissknife.ddd.domain

/** A domain query: an instruction that requests data without side effects. */
interface Query : Instruction {

    companion object
}