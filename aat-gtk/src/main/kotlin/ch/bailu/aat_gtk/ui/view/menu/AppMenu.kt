package ch.bailu.aat_gtk.ui.view.menu

import ch.bailu.aat_gtk.ui.view.menu.model.Item
import ch.bailu.aat_gtk.ui.view.menu.model.LabelItem
import ch.bailu.aat_gtk.ui.view.menu.model.Menu
import ch.bailu.aat_gtk.ui.view.menu.model.SeparatorItem
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.gtk.gtk.ApplicationWindow

class AppMenu(window: ApplicationWindow, private val services: ServicesInterface) : Menu() {

    private class TrackerItem(private val services: ServicesInterface, label: String, onSelect: (Item) -> Unit) : LabelItem(label, onSelect) {
        override val label: String
            get() = services.trackerService.startStopText
    }

    init {
        add(LabelItem("Map") {} )
        add(LabelItem("Cockpit") {})
        add(LabelItem("Tracks & Overlays") {})
        add(LabelItem("Preferences") {})
        add(SeparatorItem())
        add(TrackerItem(services, "") {
            services.trackerService.onStartStop()
        })
        add(SeparatorItem())
        add(LabelItem("PinePhone low res") {window.resize(720 / 2, 1440 / 2)})
        add(LabelItem("PinePhone hight res") {window.resize(720, 1440)})
    }
}