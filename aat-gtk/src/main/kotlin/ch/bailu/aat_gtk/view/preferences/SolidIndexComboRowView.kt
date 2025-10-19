package ch.bailu.aat_gtk.view.preferences

import ch.bailu.aat_gtk.util.extensions.setTooltipText
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
        val stringList = StringList(strings)
        layout.model = stringList.asListModel()

        layout.onNotify {
            if ("selected" == it.name.toString()) { // Property "selected" has changed
                blockUpdate = true
                solid.index = layout.selected
                blockUpdate = false
            }
        }
        layout.onDestroy {
            layout.disconnectSignals()
            solid.unregister(this)
            strings.destroy()
            stringList.unref()
        }
        layout.setTooltipText(solid)
        layout.selected = solid.index
        solid.register(this)
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {
        if (!blockUpdate && solid.hasKey(key))  {
            layout.selected = solid.index
        }
    }
}
