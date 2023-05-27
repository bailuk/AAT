package ch.bailu.aat_gtk.view.dialog

import ch.bailu.aat_lib.app.AppConfig
import ch.bailu.gtk.adw.AboutWindow
import ch.bailu.gtk.gtk.License
import ch.bailu.gtk.gtk.Window

object About {
    fun show(window: Window) {
        AboutWindow().apply {
            val config = AppConfig.getInstance()
            setApplicationIcon(config.applicationId)
            setApplicationName(config.longName)
            setVersion(config.versionName)
            setWebsite(config.website)
            setCopyright(config.copyright)
            licenseType = License.GPL_3_0
            transientFor = window
            modal = true
            show()
        }
    }
}
