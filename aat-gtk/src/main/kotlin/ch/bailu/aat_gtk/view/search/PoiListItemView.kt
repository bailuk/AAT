package ch.bailu.aat_gtk.view.search

import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.util.extensions.margin
import ch.bailu.aat_lib.api.poi.PoiListItem
import ch.bailu.gtk.gtk.*
import ch.bailu.gtk.type.Str

class PoiListItemView(listItem: ListItem) {
    private val checkBox = CheckButton()
    private val label = Label(Str.NULL)

    init {
        listItem.child = Box(Orientation.HORIZONTAL, 0).apply {
            margin(Layout.MARGIN)
            append(label)
            append(checkBox)
        }
    }

    fun set(entry: PoiListItem) {
        if (entry.isSummary()) {
            checkBox.visible = false
            label.setMarkup("<b>${entry.title}</b>")
            label.visible = true
        } else {
            label.visible = false
            checkBox.active = entry.isSelected()
            checkBox.setLabel(entry.title)
            checkBox.visible = true

            // TODO unlink reference to lambda here
            checkBox.onToggled {
                entry.setSelected(checkBox.active)
                // TODO update filter list here to remove unselected from list?
            }
        }
    }

    fun onTeardown() {
        // TODO unlink reference to lambda here
    }
}
