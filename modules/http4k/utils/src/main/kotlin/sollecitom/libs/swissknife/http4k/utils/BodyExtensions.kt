package sollecitom.libs.swissknife.http4k.utils

import org.http4k.core.ContentType
import org.http4k.core.Request
import org.http4k.core.Response
import org.json.JSONArray
import org.json.JSONObject

fun Request.body(json: JSONObject) = body(json.toString()).contentType(ContentType.APPLICATION_JSON).replaceHeader(HttpHeaders.ContentLength, json.toString().toByteArray().size.toString())
fun Request.body(json: JSONArray) = body(json.toString()).contentType(ContentType.APPLICATION_JSON).replaceHeader(HttpHeaders.ContentLength, json.toString().toByteArray().size.toString())

fun Response.body(json: JSONObject) = body(json.toString()).contentType(ContentType.APPLICATION_JSON).replaceHeader(HttpHeaders.ContentLength, json.toString().toByteArray().size.toString())
fun Response.body(json: JSONArray) = body(json.toString()).contentType(ContentType.APPLICATION_JSON).replaceHeader(HttpHeaders.ContentLength, json.toString().toByteArray().size.toString())
