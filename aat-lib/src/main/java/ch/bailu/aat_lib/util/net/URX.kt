package ch.bailu.aat_lib.util.net

import java.net.MalformedURLException
import java.net.URL
import javax.annotation.Nonnull

class URX(private val string: String) {
    private var url: URL? = null
    private var urlException: MalformedURLException? = null


    @Nonnull
    override fun toString(): String {
        return string
    }

    @Throws(MalformedURLException::class)
    fun toURL(): URL {
        throwIfException()
        setURL()

        return getUrlOrThrow()
    }

    private fun getUrlOrThrow(): URL {
        val result = url

        if (result == null) {
            val exception = MalformedURLException(string)
            urlException = exception
            throw exception
        } else {
            return result
        }
    }

    private fun throwIfException() {
        val exception = urlException
        if (exception != null) {
            throw exception
        }
    }

    private fun setURL() {
        if (url == null) {
            try {
                url = URL(toString())
            } catch (e: MalformedURLException) {
                urlException = e
                throw e
            }
        }
    }
}
