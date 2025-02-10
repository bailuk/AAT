package ch.bailu.aat_gtk.config

import ch.bailu.aat_gtk.config.Strings.appIdName
import ch.bailu.aat_lib.util.fs.AppDirectory

object Environment {

    private val userHome = System.getProperty("user.home")

    val configHome = getEnv("XDG_CONFIG_HOME", "$userHome/.config", appIdName)
    val dataHome   = getEnv("XDG_DATA_HOME"  , "$userHome", AppDirectory.DIR_AAT_DATA)
    val cacheHome  = getEnv("XDG_CACHE_HOME" , "$userHome/.cache", appIdName)

    private fun getEnv(name: String, fallBack: String, subdir: String): String {
        return "${getEnv(name, fallBack)}/$subdir"
    }

    private fun getEnv(name: String, fallBack: String): String {
        val result: String? = System.getenv(name)?.trim()
        if (result is String && result.isNotEmpty()) {
            return result
        }
        return fallBack
    }
}
