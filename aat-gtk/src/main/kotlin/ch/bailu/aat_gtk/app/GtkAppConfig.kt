package ch.bailu.aat_gtk.app

import ch.bailu.aat_lib.app.AppConfig

object GtkAppConfig : AppConfig() {
    override val appVersionName = "${super.appVersionName}-alpha"
    override val isRelease = false
}
