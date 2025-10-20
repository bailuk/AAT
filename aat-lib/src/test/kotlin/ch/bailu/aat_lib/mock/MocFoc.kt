package ch.bailu.aat_lib.mock

import ch.bailu.foc.FocFile
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream

class MockFoc(file: String) : FocFile(file) {

    private val outputStream: OutputStream = ByteArrayOutputStream()

    override fun openW(): OutputStream {
        return outputStream
    }

    override fun openR(): InputStream {
        return ByteArrayInputStream(getContent().toByteArray(Charsets.UTF_8))
    }

    fun getContent(): String {
        return (outputStream as ByteArrayOutputStream).toString(Charsets.UTF_8.name())
    }

    fun getLines(): List<String> {
        return getContent().lines()
    }
}
