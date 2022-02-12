package ch.bailu.aat_gtk.ui.view.menu

import ch.bailu.aat_gtk.ui.view.menu.model.Group
import ch.bailu.aat_gtk.ui.view.menu.model.Item
import ch.bailu.aat_gtk.ui.view.menu.model.Menu
import ch.bailu.aat_gtk.ui.view.menu.model.Type
import ch.bailu.aat_lib.preferences.SolidCheckList

class SolidCheckMenu(private val sindex: SolidCheckList): Menu() {
    private val solidGroup = Group()
    private val strings = sindex.stringArray

    private inner class CheckItem(val index: Int, onSelected: (Item) -> Unit) : Item(Type.CHECK, onSelected) {
        override val label: String
            get() = strings[index]

        override val selected: Boolean
            get() = sindex.enabledArray[index]

        override val group: Group
            get() = solidGroup

    }

    init {
        for (index in sindex.enabledArray.indices) {
            add(CheckItem(index) {
                if (it is CheckItem) {
                    sindex.setEnabled(it.index, !it.selected)
                }
            })
        }
    }
}
