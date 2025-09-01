package ch.bailu.aat_gtk.view.solid

import ch.bailu.aat_gtk.util.extensions.setTooltipText
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.SolidIndexList
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.gtk.adw.ComboRow
import ch.bailu.gtk.gtk.StringList
import ch.bailu.gtk.type.Strs

class SolidIndexComboRowView(private val solid: SolidIndexList) : OnPreferencesChanged {
    val layout = ComboRow()
    private var blockUpdate = false

    init {
        layout.setTitle(solid.getLabel())

        val strings = Strs.nullTerminated(solid.getStringArray())
        layout.model = StringList(strings).asListModel()

        layout.onActivate {
            AppLog.d(this, "onActivate")
        }
        layout.onNotify {
            if ("selected" == it.name.toString()) { // Property "selected" has changed
                blockUpdate = true
                solid.index = layout.index
                blockUpdate = false
            }
        }
        layout.setTooltipText(solid)
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {
        if (!blockUpdate && solid.hasKey(key))  {
            layout.selected = solid.index
        }
    }
}
