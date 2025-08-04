package ch.bailu.aat_gtk.view.toplevel

import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.controller.UiControllerInterface
import ch.bailu.aat_gtk.view.solid.SolidPresetComboView
import ch.bailu.aat_gtk.view.tracker.TrackerControllerView
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.dispatcher.Dispatcher
import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.gtk.adw.Clamp
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Orientation

class CockpitPage(appContext: AppContext, uiController: UiControllerInterface, dispatcher: Dispatcher) {
    private val cockpitView = CockpitView(appContext).apply {addDefaults((dispatcher))}.scrolledWindow

    private val clamp = Clamp().apply {
        maximumSize = Layout.WINDOW_HEIGHT
        asOrientable().orientation = Orientation.VERTICAL
        child = cockpitView
    }

    val box = Box(Orientation.VERTICAL, Layout.MARGIN).apply {
        val preset = SolidPresetComboView()
        dispatcher.addTarget(preset, InfoID.TRACKER)

        append(preset.layout)
        append(TrackerControllerView(appContext.services, dispatcher, uiController).box)
        append(clamp)
    }
}
