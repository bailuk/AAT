package ch.bailu.aat_gtk.ui.view.menu

import ch.bailu.aat_gtk.ui.view.menu.model.Group
import ch.bailu.aat_gtk.ui.view.menu.model.Menu
import ch.bailu.aat_gtk.ui.view.menu.model.RadioItem
import ch.bailu.aat_lib.preferences.SolidIndexList

class SolidIndexMenu(sindex: SolidIndexList) : Menu() {

    init {
        val group = Group()

        for ((index, label) in sindex.stringArray.withIndex()) {
            add(RadioItem(group, label, index == sindex.index) {
                if (it is RadioItem) {
                    sindex.index = it.index
                }
            })
        }
    }
}