package ch.bailu.aat_gtk.ui.view.menu

import ch.bailu.aat_gtk.ui.view.MainStackView
import ch.bailu.aat_gtk.ui.view.menu.model.*
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.gtk.gtk.ApplicationWindow

class AppMenu(window: ApplicationWindow, private val services: ServicesInterface, stack: MainStackView) : Menu() {

    private inner class TrackerItem(onSelected: (Item) -> Unit) : LabelItem(onSelected) {
        override val label: String
            get() = services.trackerService.startStopText
    }

    init {
        add(FixedLabelItem(Res.str().intro_map()) {stack.showMap()} )
        add(FixedLabelItem("Cockpit") {stack.showCockpit()})
        add(FixedLabelItem("Tracks & Overlays") {stack.showFiles()})
        add(FixedLabelItem(Res.str().intro_settings()) {stack.showPreferences()})

        add(SeparatorItem(Menu().apply {
            add(TrackerItem {
                services.trackerService.onStartStop()
            })
        }))


        add(SeparatorItem(Menu().apply {
            add(FixedLabelItem("PinePhone low res") {window.setDefaultSize(720 / 2, 1440 / 2)})
            add(FixedLabelItem("PinePhone hight res") {window.setDefaultSize(720, 1440)})
        }))
    }
}