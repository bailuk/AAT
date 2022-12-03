package ch.bailu.aat_gtk.view.dialog

import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_gtk.lib.icons.IconMap
import ch.bailu.aat_lib.resources.ToDo
import ch.bailu.gtk.gtk.*

object About {
    fun show(window: Window) {
        AboutDialog().apply {
            logo = IconMap.getPaintable("app-icon", 120)
            programName = Strings.appName
            version = Strings.version
            website = Strings.website
            copyright = Strings.copyright
            licenseType = License.GPL_3_0
            titlebar = createHeaderBar(this)
            transientFor = window
            modal = true
            show()
        }
    }

    private fun createHeaderBar(aboutDialog: AboutDialog): HeaderBar {
        return HeaderBar().apply {
            showTitleButtons = false
            packEnd(Button().apply {
                setLabel(ToDo.translate("Close"))
                onClicked {
                    aboutDialog.close()
                }
            })
        }
    }
}
