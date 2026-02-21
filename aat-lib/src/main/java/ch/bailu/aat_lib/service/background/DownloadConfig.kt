package ch.bailu.aat_lib.service.background

import ch.bailu.aat_lib.app.AppConfig
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class DownloadConfig(config: AppConfig) {
    private val userAgentValue: String = config.userAgent

    fun createBuffer(): ByteArray {
        return ByteArray(IO_BUFFER_SIZE)
    }

    @Throws(IOException::class)
    fun openConnection(url: URL): HttpURLConnection {
        val connection = url.openConnection() as HttpURLConnection
        connection.connectTimeout = TIMEOUT
        connection.readTimeout = TIMEOUT
        connection.setRequestProperty(USER_AGENT_KEY, userAgentValue)
        return connection
    }

    @Throws(IOException::class)
    fun openInput(connection: HttpURLConnection): InputStream {
        return connection.getInputStream()
    }

    companion object {
        private const val TIMEOUT = 30 * 1000
        private const val USER_AGENT_KEY = "User-Agent"
        private const val IO_BUFFER_SIZE = 8 * 1024
    }
}
