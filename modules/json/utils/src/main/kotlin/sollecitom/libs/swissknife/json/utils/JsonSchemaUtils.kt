package sollecitom.libs.swissknife.json.utils

import sollecitom.libs.swissknife.resource.utils.ResourceLoader.openAsStream
import com.github.erosb.jsonsKema.JsonParser
import com.github.erosb.jsonsKema.SchemaClient
import com.github.erosb.jsonsKema.SchemaLoader
import com.github.erosb.jsonsKema.SchemaLoaderConfig
import org.json.JSONObject
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.URI

private val initialBaseURI = URI("mem://input")
private val schemaClient: SchemaClient by lazy { CachedSchemaClient(CustomSchemaClient(initialBaseURI.toString())) }

fun jsonSchemaAt(location: String): JsonSchema = openAsStream(location).use {

    val locationBytes = it.readAllBytes()
    val jsonSchema = JSONObject(String(locationBytes))
    val schemaJson = JsonParser(jsonSchema.toString()).parse()
    val schema = SchemaLoader(schemaJson, SchemaLoaderConfig(schemaClient, initialBaseURI)).load()
    JsonSchema(schema, jsonSchema)
}

fun JSONObject.asSchema(): JsonSchema {

    val parsedJsonSchema = JsonParser(toString()).parse()
    val resolvedSchema = SchemaLoader(parsedJsonSchema, SchemaLoaderConfig(schemaClient, initialBaseURI)).load()
    return JsonSchema(resolvedSchema, this)
}

private class CachedSchemaClient(private val delegate: SchemaClient) : SchemaClient by delegate {

    private val cache: MutableMap<URI, ByteArray> = mutableMapOf()

    override fun get(uri: URI): InputStream = ByteArrayInputStream(
        cache.computeIfAbsent(uri) {
            val out = ByteArrayOutputStream()
            delegate.get(it).transferTo(out)
            out.toByteArray()
        }
    )
}

private class CustomSchemaClient(private val initialBaseURI: String) : SchemaClient {

    override fun get(uri: URI): InputStream {
        val resourceName = uri.toString().removePrefix(initialBaseURI).removePrefix("/")
        return openAsStream(resourceName)
    }
}