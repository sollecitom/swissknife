package sollecitom.libs.swissknife.avro.schema.checker

import org.apache.avro.Schema
import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRule

/**
 * Compliance rule for a topic's value schema: following the record fields in [eventsFieldPath] must reach a union whose branches are all records (one per event type).
 * New event types can then be added as union branches, which a BACKWARD_TRANSITIVE registry accepts.
 */
data class TopicEventUnionRule(val eventsFieldPath: List<String> = listOf("data", "envelope")) : ComplianceRule<Schema> {

    init {
        require(eventsFieldPath.isNotEmpty()) { "The path to the events union must name at least one field" }
    }

    override fun invoke(target: Schema): ComplianceRule.Result<Schema> {

        val events = eventsFieldPath.fold<String, Schema?>(target) { schema, fieldName -> schema?.takeIf { it.type == Schema.Type.RECORD }?.getField(fieldName)?.schema() }
        val isUnionOfRecords = events != null && events.type == Schema.Type.UNION && events.types.all { it.type == Schema.Type.RECORD }
        return ComplianceRule.Result.withViolationOrNull(if (isUnionOfRecords) null else Violation(topicSchemaName = target.fullName, eventsFieldPath = eventsFieldPath))
    }

    data class Violation(val topicSchemaName: String, val eventsFieldPath: List<String>) : ComplianceRule.Result.Violation<Schema> {

        override val message = "Topic value schema '$topicSchemaName' should reach its events through a union of records at '${eventsFieldPath.joinToString(".")}', but doesn't"
    }
}
