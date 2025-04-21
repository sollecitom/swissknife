package sollecitom.libs.swissknife.avro.serialization.utils

import sollecitom.libs.swissknife.resource.utils.ResourceLoader
import org.apache.avro.Schema

@Suppress("DEPRECATION") // the newest version of Avro isn't supported by Pulsar yet
fun avroSchemaAt(relativePath: String, referencedTypes: Map<String, Schema> = emptyMap()): Schema = Schema.Parser().addTypes(referencedTypes).parse(ResourceLoader.readAsText(relativePath))