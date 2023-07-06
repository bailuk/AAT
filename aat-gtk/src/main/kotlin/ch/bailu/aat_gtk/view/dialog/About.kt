package ch.bailu.aat_gtk.view.dialog

import ch.bailu.aat_lib.app.AppConfig
import ch.bailu.gtk.adw.AboutWindow
import ch.bailu.gtk.gtk.License
import ch.bailu.gtk.gtk.Window

object About {
    fun show(window: Window) {
        AboutWindow().apply {
            val config = AppConfig.getInstance()

            setApplicationIcon(config.appId)
            setApplicationName(config.appLongName)
            setVersion(config.appVersionName)
            setWebsite(config.appWebsite)
            setCopyright(config.appCopyright)
            licenseType = License.GPL_3_0
            transientFor = window
            modal = true
            show()
        }
    }
}
