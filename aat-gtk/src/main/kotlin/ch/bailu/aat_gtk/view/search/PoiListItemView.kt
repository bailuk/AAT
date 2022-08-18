package ch.bailu.aat_gtk.view.search

import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.lib.extensions.margin
import ch.bailu.aat_gtk.lib.extensions.setLabel
import ch.bailu.aat_gtk.lib.extensions.setMarkup
import ch.bailu.aat_lib.search.poi.PoiListItem
import ch.bailu.gtk.GTK
import ch.bailu.gtk.gtk.*
import ch.bailu.gtk.type.Str

class PoiListItemView(listItem: ListItem) {
    private val checkBox = CheckButton()
    private val label = Label(Str.NULL)

    init {
        listItem.child = Box(Orientation.HORIZONTAL, 0).apply {
            margin(Layout.margin)
            append(label)
            append(checkBox)
        }
    }

    fun set(entry: PoiListItem) {
        if (entry.isSummary) {
            checkBox.visible = GTK.FALSE
            label.setMarkup("<b>${entry.title}</b>")
            label.visible = GTK.TRUE
        } else {
            label.visible = GTK.FALSE
            checkBox.active = GTK.IS(entry.isSelected)
            checkBox.setLabel(entry.title)
            checkBox.visible = GTK.TRUE

            // TODO unlink reference to lambda here
            checkBox.onToggled {
                entry.isSelected = GTK.IS(checkBox.active)
                // TODO update filter list here to remove unselected from list?
            }
        }
    }

    fun onTeardown() {
        // TODO unlink reference to lambda here
    }
}
