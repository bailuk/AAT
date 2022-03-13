package ch.bailu.aat_gtk.view.menu

import ch.bailu.aat_gtk.view.menu.model.FixedLabelItem
import ch.bailu.aat_gtk.view.menu.model.Menu
import ch.bailu.aat_lib.preferences.SolidFile

class SolidFileListMenu (private val solidFile: SolidFile): Menu(solidFile.label) {
    private val strings = solidFile.buildSelection(ArrayList())

    init {
        for (string in strings) {
            add(FixedLabelItem(string) {
                solidFile.setValueFromString(string)
            })
        }
    }
}