package sollecitom.libs.swissknife.logger.json.formatter

import sollecitom.libs.swissknife.json.utils.JsonSchema
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.logger.core.LogEntry

private val logEntrySchema by lazy { jsonSchemaAt("LogEntry.json") }

val LogEntry.Companion.jsonSchema: JsonSchema get() = logEntrySchema
