package ch.bailu.aat_gtk.view.menu

import ch.bailu.aat_gtk.view.menu.model.*
import ch.bailu.aat_lib.preferences.SolidCheckList

class SolidCheckMenu(private val sindex: SolidCheckList): Menu(sindex.label) {
    private val strings = sindex.stringArray

    private inner class MyCheckItem(val index: Int, onSelected: (Item) -> Unit) : CheckItem(onSelected) {
        override val label: String
            get() = strings[index]

        override val selected: Boolean
            get() = sindex.enabledArray[index]
    }

    init {
        for (index in sindex.enabledArray.indices) {
            add(MyCheckItem(index) {
                if (it is MyCheckItem) {
                    sindex.setEnabled(it.index, !it.selected)
                }
            })
        }
    }
}
