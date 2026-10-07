package sollecitom.libs.swissknife.json.utils

import sollecitom.libs.swissknife.resource.utils.ResourceLoader.openAsStream
import com.github.erosb.jsonsKema.JsonParser
import com.github.erosb.jsonsKema.PrepopulatedSchemaClient
import com.github.erosb.jsonsKema.SchemaClient
import com.github.erosb.jsonsKema.SchemaLoader
import com.github.erosb.jsonsKema.SchemaLoaderConfig
import org.json.JSONObject
import java.io.InputStream
import java.net.URI

private val initialBaseURI = URI("mem://input")
private val schemaClient: SchemaClient by lazy { CustomSchemaClient(initialBaseURI.toString()).let(::PrepopulatedSchemaClient) }

/** Loads and parses a JSON Schema from a classpath resource at the given [location]. */
fun jsonSchemaAt(location: String): JsonSchema = openAsStream(location).use { it.readAllBytes().decodeToString().let(::JSONObject).asSchema() }

/** Interprets this JSONObject as a JSON Schema definition. */
fun JSONObject.asSchema(): JsonSchema {

    val parsedJsonSchema = toString().let(::JsonParser).parse()
    val resolvedSchema = SchemaLoader(parsedJsonSchema, SchemaLoaderConfig(schemaClient, initialBaseURI)).load()
    return JsonSchema(resolvedSchema, this)
}

private class CustomSchemaClient(private val initialBaseURI: String) : SchemaClient {

    override fun get(uri: URI): InputStream {
        val resourceName = uri.toString().removePrefix(initialBaseURI).removePrefix("/")
        return openAsStream(resourceName)
    }
}