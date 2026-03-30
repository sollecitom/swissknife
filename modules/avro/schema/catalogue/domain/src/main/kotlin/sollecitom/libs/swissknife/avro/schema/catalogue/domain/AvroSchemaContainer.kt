package sollecitom.libs.swissknife.avro.schema.catalogue.domain

import sollecitom.libs.swissknife.core.domain.text.Name
import org.apache.avro.Schema

/** A hierarchical container of Avro schemas, organized by namespace. */
interface AvroSchemaContainer {

    val namespace: Name
    val nestedContainers: Set<AvroSchemaContainer> get() = emptySet()
    /** All schemas directly in this container (not including nested containers). */
    val all: Sequence<Schema>
}

/** All schemas from this container and all nested containers, flattened into a single set. */
val AvroSchemaContainer.schemasAndDependencies: Set<Schema> get() = (all + nestedContainers.flatMap { it.schemasAndDependencies }).toSet()