package ch.bailu.aat_gtk.config

import ch.bailu.aat_gtk.config.Strings.appIdName

object Environment {

    val userHome: String = System.getProperty("user.home")
    val configDirectory = run {
        val xdgConfigHome: String? = System.getenv("XDG_CONFIG_HOME")?.trim()

        if (xdgConfigHome is String && xdgConfigHome.isNotEmpty()) {
            "$xdgConfigHome/$appIdName"
        } else {
            "$userHome/.config/$appIdName"
        }
    }
}
