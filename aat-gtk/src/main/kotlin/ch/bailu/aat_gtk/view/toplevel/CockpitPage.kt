package ch.bailu.aat_gtk.view.toplevel

import ch.bailu.aat_gtk.config.Icons
import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_gtk.lib.extensions.margin
import ch.bailu.aat_gtk.view.TrackerSplitButton
import ch.bailu.aat_gtk.view.UiController
import ch.bailu.aat_gtk.view.description.DescriptionLabelTextView
import ch.bailu.aat_gtk.view.map.GtkCustomMapView
import ch.bailu.aat_gtk.view.solid.SolidPresetComboView
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.description.GpsStateDescription
import ch.bailu.aat_lib.description.TrackerStateDescription
import ch.bailu.aat_lib.dispatcher.Dispatcher
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.preferences.map.SolidPositionLock
import ch.bailu.gtk.adw.Clamp
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Button
import ch.bailu.gtk.gtk.Orientation

class CockpitPage(appContext: AppContext, uiController: UiController, dispatcher: Dispatcher) {
    private val cockpitView = CockpitView(appContext).apply {addDefaults((dispatcher))}.scrolledWindow

    private val clamp = Clamp().apply {
        maximumSize = Layout.windowHeight
        asOrientable().orientation = Orientation.VERTICAL
        child = cockpitView
    }

    private val buttons = Box(Orientation.HORIZONTAL, Layout.margin).apply {
        margin(Layout.margin)
        append(Box(Orientation.HORIZONTAL, 0).apply {
            addCssClass(Strings.linked)
            append(Button().apply {
                iconName = Icons.viewContinuousSymbolic
                onClicked {
                    uiController.showDetail()
                    uiController.showInDetail(InfoID.TRACKER)
                }
            })
            append(Button().apply {
                iconName = Icons.zoomFitBestSymbolic
                onClicked {
                    uiController.showMap()
                    SolidPositionLock(appContext.storage, GtkCustomMapView.KEY).value = true
                }
            })
        })
        append(TrackerSplitButton(appContext.services, dispatcher).button)
    }

    private val status = Box(Orientation.HORIZONTAL, 0).apply {
        append(DescriptionLabelTextView(TrackerStateDescription()).apply { dispatcher.addTarget(this, InfoID.TRACKER) }.layout)
        append(DescriptionLabelTextView(GpsStateDescription()).apply { dispatcher.addTarget(this, InfoID.LOCATION) }.layout)
    }

    val box = Box(Orientation.VERTICAL, Layout.margin).apply {
        val preset = SolidPresetComboView()
        dispatcher.addTarget(preset, InfoID.TRACKER)

        append(preset.layout)
        append(Box(Orientation.HORIZONTAL, 0).apply {
            append(Box(Orientation.VERTICAL, 0).apply {
                append(buttons.apply {
                    vexpand = false
                    hexpand = true
                })
            })
            append(status)
        })
        append(clamp)

    }
}
