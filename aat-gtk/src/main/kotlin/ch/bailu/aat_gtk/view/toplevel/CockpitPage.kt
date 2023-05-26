package ch.bailu.aat_gtk.view.toplevel

import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_gtk.lib.extensions.margin
import ch.bailu.aat_gtk.view.UiController
import ch.bailu.aat_gtk.view.map.GtkCustomMapView
import ch.bailu.aat_lib.dispatcher.Dispatcher
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.preferences.map.SolidPositionLock
import ch.bailu.gtk.adw.Clamp
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Button
import ch.bailu.gtk.gtk.Orientation

class CockpitPage(uiController: UiController, dispatcher: Dispatcher) {
    private val cockpitView = CockpitView().apply {addDefaults((dispatcher))}.scrolledWindow

    private val clamp = Clamp().apply {
        maximumSize = Layout.windowHeight
        asOrientable().orientation = Orientation.VERTICAL
        child = cockpitView
    }

    private val buttons = Box(Orientation.HORIZONTAL, 0).apply {
        margin(Layout.margin)
        addCssClass(Strings.linked)
        append(Button().apply {
            iconName = Strings.iconDetail
            onClicked {
                uiController.showDetail()
                uiController.showInDetail(InfoID.TRACKER)
            }
        })
        append(Button().apply {
            iconName = Strings.iconCenter
            onClicked {
                uiController.showMap()
                SolidPositionLock(GtkAppContext.storage, GtkCustomMapView.KEY).value = true
            }
        })
    }

    val box = Box(Orientation.VERTICAL, Layout.margin).apply {
        append(buttons)
        append(clamp)
    }
}
