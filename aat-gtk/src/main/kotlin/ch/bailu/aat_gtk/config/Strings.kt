package ch.bailu.aat_gtk.config

import ch.bailu.aat_lib.Configuration
import ch.bailu.gtk.type.Str

object Strings {
    const val appIdName = "aat-gtk"
    const val appPreferencesNode = "ch/bailu/aat"

    val appName = Str(Configuration.appName)
    val version = Str(Configuration.appVersionName)
    val copyright = Str(Configuration.appCopyright)
    val website = Str(Configuration.appWebsite)

    // CSS
    val linked = Str("linked")

    // Defaults
    val empty = Str("")
}
