package ch.bailu.aat_gtk.ui.view.menu

import ch.bailu.aat_gtk.ui.view.menu.model.Item
import ch.bailu.aat_gtk.ui.view.menu.model.LabelItem
import ch.bailu.aat_gtk.ui.view.menu.model.Menu
import ch.bailu.aat_lib.preferences.SolidIndexList

class SolidSelectMenu (sindex: SolidIndexList, val function: ()-> Unit) : Menu() {

    private class IndexLabelItem(label: String, val index: Int, onSelect: (Item) -> Unit): LabelItem(label, onSelect)

    init {
        for ((index, label) in sindex.stringArray.withIndex()) {
            add(IndexLabelItem(label, index) {
                if (it is IndexLabelItem) {
                    sindex.index = it.index
                    function()
                }
            })
        }
    }
}