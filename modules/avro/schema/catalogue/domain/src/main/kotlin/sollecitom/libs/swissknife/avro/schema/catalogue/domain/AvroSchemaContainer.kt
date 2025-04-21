package sollecitom.libs.swissknife.avro.schema.catalogue.domain

import sollecitom.libs.swissknife.core.domain.text.Name
import org.apache.avro.Schema

interface AvroSchemaContainer {

    val namespace: Name
    val nestedContainers: Set<AvroSchemaContainer> get() = emptySet()
    val all: Sequence<Schema>
}

val AvroSchemaContainer.schemasAndDependencies: Set<Schema> get() = (all + nestedContainers.flatMap { it.schemasAndDependencies }).toSet()