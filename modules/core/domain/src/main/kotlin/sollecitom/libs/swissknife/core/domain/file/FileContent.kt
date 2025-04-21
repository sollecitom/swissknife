package sollecitom.libs.swissknife.core.domain.file

import java.io.InputStream
import java.net.URI

sealed interface FileContent {

    val length: Int
    val format: Format
    fun open(): InputStream

    data class Inline(val bytes: ByteArray, override val format: Format) : FileContent {

        override val length get() = bytes.size

        override fun open() = bytes.inputStream()

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Inline

            if (!bytes.contentEquals(other.bytes)) return false
            if (format != other.format) return false

            return true
        }

        override fun hashCode(): Int {
            var result = bytes.contentHashCode()
            result = 31 * result + format.hashCode()
            return result
        }

        override fun toString() = "Inline(content=${bytes.contentToString()}, format=$format)"

        companion object
    }

    data class Remote(override val length: Int, val contentURI: URI, override val format: Format) : FileContent {

        override fun open(): InputStream = contentURI.toURL().openStream()

        companion object
    }

    sealed class Format(val name: String, val extension: String, val mimeType: String) {

        data object CSV : Format("CSV", "csv", "text/csv")
        data object PDF : Format("PDF", "pdf", "application/pdf")

        companion object
    }

    companion object
}

fun FileContent.Format.Companion.withName(name: String): FileContent.Format = when(name) {

    FileContent.Format.CSV.name -> FileContent.Format.CSV
    FileContent.Format.PDF.name -> FileContent.Format.PDF
    else -> error("Unknown format name '$name'")
}