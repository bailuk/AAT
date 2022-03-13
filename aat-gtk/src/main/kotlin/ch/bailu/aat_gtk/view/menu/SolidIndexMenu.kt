package ch.bailu.aat_gtk.view.menu

import ch.bailu.aat_gtk.view.menu.model.*
import ch.bailu.aat_lib.preferences.SolidIndexList

class SolidIndexMenu(private val sindex: SolidIndexList) : Menu() {
    private val solidGroup = Group()
    private val strings = sindex.stringArray

    private inner class MyRadioItem(val index: Int, onSelected: (Item) -> Unit) : RadioItem(onSelected, solidGroup) {

        override val label: String
            get() = strings[index]

        override val selected: Boolean
            get() = index == sindex.index
    }

    init {
        for (index in 0 until sindex.length()) {
            add(MyRadioItem(index) {
                if (it is MyRadioItem) {
                    sindex.index = it.index
                }
            })
        }
    }
}
