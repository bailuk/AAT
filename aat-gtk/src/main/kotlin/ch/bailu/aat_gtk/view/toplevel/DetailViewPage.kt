package ch.bailu.aat_gtk.view.toplevel

import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_gtk.config.Icons
import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_gtk.controller.UiController
import ch.bailu.aat_gtk.lib.extensions.margin
import ch.bailu.aat_lib.dispatcher.Dispatcher
import ch.bailu.aat_lib.dispatcher.usage.SelectableUsageTracker
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Button
import ch.bailu.gtk.gtk.Orientation

class DetailViewPage(uiController: UiController, dispatcher: Dispatcher, private val usageTracker: SelectableUsageTracker) {



    private val detailView = DetailView(dispatcher, usageTracker, GtkAppContext.storage).scrolled

    val box = Box(Orientation.VERTICAL, 0).apply {
        append(Box(Orientation.HORIZONTAL, 0).apply {
            margin(Layout.margin)
            addCssClass(Strings.linked)

            append(Button().apply {
                iconName = Icons.zoomFitBestSymbolic
                onClicked {
                    uiController.showMap()
                    // TODO: uiController.frameInMap(selectedSource.info)
                }
            })

            append(Button().apply {
                iconName = Icons.findLocationSymbolic
                onClicked {
                    uiController.showMap()
                    // TODO: uiController.centerInMap(selectedSource.info)
                }
            })
        })

        append(detailView)

    }

    init {
        usageTracker.select(InfoID.TRACKER)
    }

    fun select(infoID: Int) {
        usageTracker.select(infoID)
    }
}
