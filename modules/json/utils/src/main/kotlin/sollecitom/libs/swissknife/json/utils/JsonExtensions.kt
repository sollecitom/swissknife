package sollecitom.libs.swissknife.json.utils

import kotlinx.datetime.Instant
import org.json.JSONArray
import org.json.JSONObject
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*

fun JSONObject.getLongOrNull(field: String): Long? = runCatching { getLong(field) }.getOrNull()

fun JSONObject.getRequiredLong(field: String): Long = getLongOrNull(field)!!

fun JSONObject.getBigIntegerOrNull(field: String): BigInteger? = runCatching { getBigInteger(field) }.getOrNull()

fun JSONObject.getRequiredBigInteger(field: String): BigInteger = getBigIntegerOrNull(field)!!

fun JSONObject.getBigDecimalOrNull(field: String): BigDecimal? = runCatching { getBigDecimal(field) }.getOrNull()

fun JSONObject.getRequiredBigDecimal(field: String): BigDecimal = getBigDecimalOrNull(field)!!

fun JSONObject.getStringOrNull(field: String): String? = runCatching { getString(field) }.getOrNull()

fun JSONObject.getJSONObjectOrNull(field: String): JSONObject? = runCatching { getJSONObject(field) }.getOrNull()

fun JSONObject.getDoubleOrNull(field: String): Double? = runCatching { getDouble(field) }.getOrNull()
fun JSONObject.getRequiredDouble(field: String): Double = getDoubleOrNull(field)!!

fun JSONObject.getIntOrNull(field: String): Int? = runCatching { getInt(field) }.getOrNull()

fun JSONObject.getRequiredInt(field: String): Int = getIntOrNull(field)!!

fun JSONObject.getArrayOrNull(field: String): JSONArray? = runCatching { getJSONArray(field) }.getOrNull()

fun JSONArray.getItem(index: Int): JSONObject? = runCatching { get(index) as JSONObject }.getOrNull()

fun JSONObject.getRequiredString(key: String): String = getString(key)!!

fun JSONObject.getBooleanOrNull(key: String): Boolean? = runCatching { getBoolean(key) }.getOrNull()
fun JSONObject.getRequiredBoolean(key: String): Boolean = getBooleanOrNull(key)!!

fun JSONObject.getRequiredJSONObject(key: String): JSONObject = getJSONObject(key)!!

fun JSONObject.getRequiredJSONArray(key: String): JSONArray = getJSONArray(key)!!

fun JSONObject.deepEquals(other: JSONObject) = toMap() == other.toMap()

fun JSONObject.deepHashCode() = toMap().hashCode()

fun JSONArray.deepEquals(other: JSONArray) = toList() == other.toList()

fun JSONArray.deepHashCode() = toList().hashCode()

fun JSONObject.putInstant(key: String, instant: Instant): JSONObject = put(key, instant.toString())

fun JSONObject.getInstantOrNull(key: String) = getStringOrNull(key)?.let(Instant.Companion::parse)
fun JSONObject.getRequiredInstant(key: String) = getInstantOrNull(key)!!

fun JSONObject.getStringArrayOrNull(key: String): List<String>? = getArrayOrNull(key)?.map { it as String }
fun JSONObject.getRequiredStringArray(key: String): List<String> = getStringArrayOrNull(key)!!

fun JSONObject.getIntArrayOrNull(key: String): List<Int>? = getArrayOrNull(key)?.map { it as Int }
fun JSONObject.getRequiredIntArray(key: String): List<Int> = getIntArrayOrNull(key)!!

fun JSONObject.putBytesAsHexString(fieldName: String, bytes: ByteArray?): JSONObject = put(fieldName, bytes?.let(HexFormat.of()::formatHex))
fun JSONObject.putBytesAsBase64String(fieldName: String, bytes: ByteArray?): JSONObject = put(fieldName, bytes?.let { Base64.getEncoder().encodeToString(it) })

fun JSONObject.getBytesFromBase64StringOrNull(key: String): ByteArray? = getStringOrNull(key)?.let { Base64.getDecoder().decode(it) }
fun JSONObject.getBytesFromBase64String(key: String): ByteArray = getBytesFromBase64StringOrNull(key)!!

fun JSONObject.getBytesFromHexStringOrNull(key: String): ByteArray? = getStringOrNull(key)?.let(HexFormat.of()::parseHex)
fun JSONObject.getBytesFromHexString(key: String): ByteArray = getBytesFromHexStringOrNull(key)!!