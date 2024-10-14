package ch.bailu.aat_gtk.config

import ch.bailu.aat_gtk.config.Strings.appIdName

object Environment {

    private val userHome = System.getProperty("user.home")

    val configHome = getEnv("XDG_CONFIG_HOME", "$userHome/.config/$appIdName")
    val dataHome   = getEnv("XDG_DATA_HOME"  , "$userHome/aat_data")
    val cacheHome  = getEnv("XDG_CACHE_HOME" , "$userHome/.cache/$appIdName")

    private fun getEnv(name: String, fallBack: String): String {
        val result: String? = System.getenv(name)?.trim()
        if (result is String && result.isNotEmpty()) {
            return result
        }
        return fallBack
    }
}
