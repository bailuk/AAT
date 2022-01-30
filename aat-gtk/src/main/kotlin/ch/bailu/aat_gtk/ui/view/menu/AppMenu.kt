package ch.bailu.aat_gtk.ui.view.menu

import ch.bailu.aat_gtk.ui.view.MainStackView
import ch.bailu.aat_gtk.ui.view.menu.model.*
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.gtk.gtk.ApplicationWindow

class AppMenu(window: ApplicationWindow, private val services: ServicesInterface, stack: MainStackView) : Menu() {

    private inner class TrackerItem(onSelected: (Item) -> Unit) : Item(Type.LABEL, onSelected) {
        override val label: String
            get() = services.trackerService.startStopText

        override val selected = false
        override val group = Group()
    }

    init {
        add(LabelItem(Res.str().intro_map()) {stack.showMap()} )
        add(LabelItem("Cockpit") {stack.showCockpit()})
        add(LabelItem("Tracks & Overlays") {stack.showFiles()})
        add(LabelItem(Res.str().intro_settings()) {stack.showPreferences()})

        add(SeparatorItem())
        add(TrackerItem {
            services.trackerService.onStartStop()
        })
        add(SeparatorItem())
        add(LabelItem("PinePhone low res") {window.setDefaultSize(720 / 2, 1440 / 2)})
        add(LabelItem("PinePhone hight res") {window.setDefaultSize(720, 1440)})
    }
}