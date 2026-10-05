package sollecitom.libs.swissknife.avro.schema.checker

import org.apache.avro.Schema

internal data class SchemaNode(val path: String, val schema: Schema)

internal fun Schema.nodes(): Sequence<SchemaNode> = sequence { visit(SchemaNode(path = fullName, schema = this@nodes), visitedNamedSchemas = mutableSetOf()) }

private suspend fun SequenceScope<SchemaNode>.visit(node: SchemaNode, visitedNamedSchemas: MutableSet<String>) {

    val schema = node.schema
    if (schema.isNamed && !visitedNamedSchemas.add(schema.fullName)) return
    yield(node)
    when (schema.type) {
        Schema.Type.RECORD -> schema.fields.forEach { field -> visit(SchemaNode(path = "${node.path}.${field.name()}", schema = field.schema()), visitedNamedSchemas) }
        Schema.Type.UNION -> schema.types.forEach { branch -> visit(node.copy(schema = branch), visitedNamedSchemas) }
        Schema.Type.ARRAY -> visit(SchemaNode(path = "${node.path}[]", schema = schema.elementType), visitedNamedSchemas)
        Schema.Type.MAP -> visit(SchemaNode(path = "${node.path}{}", schema = schema.valueType), visitedNamedSchemas)
        else -> Unit
    }
}

private val Schema.isNamed: Boolean get() = type == Schema.Type.RECORD || type == Schema.Type.ENUM || type == Schema.Type.FIXED
