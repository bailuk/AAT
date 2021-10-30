package ch.bailu.aat_gtk.ui.view.menu

import ch.bailu.aat_gtk.ui.view.menu.model.LabelItem
import ch.bailu.aat_gtk.ui.view.menu.model.Menu
import ch.bailu.aat_gtk.ui.view.menu.model.SeparatorItem
import ch.bailu.gtk.gtk.ApplicationWindow

class AppMenu(window: ApplicationWindow) : Menu() {
    init {
        add(LabelItem("Map") {} )
        add(LabelItem("Cockpit") {})
        add(LabelItem("Tracks & Overlays") {})
        add(LabelItem("Preferences") {})
        add(SeparatorItem())
        add(LabelItem("PinePhone low res") {window.resize(720 / 2, 1440 / 2)})
        add(LabelItem("PinePhone hight res") {window.resize(720, 1440)})
    }
}