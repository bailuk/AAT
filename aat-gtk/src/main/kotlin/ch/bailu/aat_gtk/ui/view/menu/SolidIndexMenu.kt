package ch.bailu.aat_gtk.ui.view.menu

import ch.bailu.aat_gtk.ui.view.menu.model.Group
import ch.bailu.aat_gtk.ui.view.menu.model.Item
import ch.bailu.aat_gtk.ui.view.menu.model.Menu
import ch.bailu.aat_gtk.ui.view.menu.model.Type
import ch.bailu.aat_lib.preferences.SolidIndexList

class SolidIndexMenu(private val sindex: SolidIndexList) : Menu() {
    private val solidGroup = Group()
    private val strings = sindex.stringArray

    private inner class RadioItem(val index: Int, onSelected: (Item) -> Unit) : Item(Type.RADIO, onSelected) {

        override val label: String
            get() = strings[index]

        override val selected: Boolean
            get() = index == sindex.index

        override val group = solidGroup
    }

    init {
        for (index in 0 until sindex.length()) {
            add(RadioItem(index) {
                if (it is RadioItem) {
                    sindex.index = it.index
                }
            })
        }
    }
}
