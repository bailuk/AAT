package ch.bailu.aat_gtk.view.solid

import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.util.extensions.margin
import ch.bailu.aat_gtk.util.extensions.setTooltipText
import ch.bailu.aat_gtk.view.menu.PopupMenuButton
import ch.bailu.aat_gtk.view.menu.provider.SolidFileSelectorMenu
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.SolidFile
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.gtk.adw.ActionRow
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.gtk.Window

class SolidDirectorySelectorView(private val solid: SolidFile, app: Application, window: Window) : OnPreferencesChanged {
    val layout = ActionRow()

    init {
        layout.setTitle(solid.getLabel())
        layout.setSubtitle(solid.getValueAsString())
        layout.setTooltipText(solid)
        layout.addSuffix(PopupMenuButton(SolidFileSelectorMenu(solid, window).apply {
            createActions(app)
        }).menuButton.apply {
            margin(Layout.MARGIN)
        })

        solid.register(this)
    }
    override fun onPreferencesChanged(storage: StorageInterface, key: String) {
        if (solid.hasKey(key)) {
            layout.setSubtitle(solid.getValueAsString())
        }
    }
}
