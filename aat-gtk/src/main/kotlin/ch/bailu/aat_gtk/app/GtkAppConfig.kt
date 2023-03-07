package ch.bailu.aat_gtk.app

import ch.bailu.aat_lib.app.AppConfig

object GtkAppConfig : AppConfig() {
    override fun getVersionName(): String {
        return "${super.getVersionName()}-alpha"
    }

    override fun isRelease(): Boolean {
        return false
    }
}
