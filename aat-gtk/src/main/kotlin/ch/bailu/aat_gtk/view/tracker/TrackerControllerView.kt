package ch.bailu.aat_gtk.view.tracker

import ch.bailu.aat_gtk.config.Icons
import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_gtk.controller.UiControllerInterface
import ch.bailu.aat_gtk.util.extensions.margin
import ch.bailu.aat_gtk.view.TrackerSplitButton
import ch.bailu.aat_gtk.view.description.DescriptionLabelTextView
import ch.bailu.aat_lib.description.GpsStateDescription
import ch.bailu.aat_lib.description.TrackerStateDescription
import ch.bailu.aat_lib.dispatcher.DispatcherInterface
import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Button
import ch.bailu.gtk.gtk.Orientation

class TrackerControllerView(services: ServicesInterface, dispatcher: DispatcherInterface, uiController: UiControllerInterface) {

    private val status = Box(Orientation.HORIZONTAL, 0).apply {
        append(DescriptionLabelTextView(TrackerStateDescription()).apply { dispatcher.addTarget(this, InfoID.TRACKER) }.layout)
        append(DescriptionLabelTextView(GpsStateDescription()).apply { dispatcher.addTarget(this, InfoID.LOCATION) }.layout)
    }

    private val buttons = Box(Orientation.HORIZONTAL, Layout.MARGIN).apply {
        margin(Layout.MARGIN)
        append(Box(Orientation.HORIZONTAL, 0).apply {
            addCssClass(Strings.linked)

            append(Button().apply {
                iconName = Icons.zoomFitBestSymbolic
                onClicked {
                    uiController.showMap()
                    uiController.frameInMap(InfoID.TRACKER)
                }
            })
            append(Button().apply {
                iconName = Icons.findLocationSymbolic
                onClicked {
                    uiController.showMap()
                    uiController.centerInMap(InfoID.TRACKER)
                }
            })
            append(Button().apply {
                iconName = Icons.viewContinuousSymbolic
                onClicked {
                    uiController.showDetail()
                    uiController.showInDetail(InfoID.TRACKER)
                }
            })
        })
        append(TrackerSplitButton(services, dispatcher).button)
    }

    val box = Box(Orientation.HORIZONTAL, 0).apply {
        append(Box(Orientation.VERTICAL, 0).apply {
            append(buttons.apply {
                vexpand = false
                hexpand = true
            })
        })
        append(status)
    }
}
