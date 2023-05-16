package ch.bailu.aat_gtk.view.toplevel

import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_gtk.dispatcher.SelectedSource
import ch.bailu.aat_gtk.lib.extensions.margin
import ch.bailu.aat_gtk.view.UiController
import ch.bailu.aat_lib.dispatcher.Dispatcher
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Button
import ch.bailu.gtk.gtk.Orientation

class DetailViewPage(uiController: UiController, dispatcher: Dispatcher) {
    private val selectedSource = SelectedSource()

    private val detailView = DetailView(selectedSource.getIntermediateDispatcher(dispatcher, InfoID.FILEVIEW, InfoID.TRACKER), GtkAppContext.storage).scrolled

    val box = Box(Orientation.VERTICAL, 0).apply {
        append(Box(Orientation.HORIZONTAL, 0).apply {
            margin(Layout.margin)
            addCssClass(Strings.linked)

            append(Button().apply {
                iconName = Strings.iconFrame
                onClicked {
                    uiController.frameInMap(selectedSource.info)
                }
            })

            append(Button().apply {
                iconName = Strings.iconCenter
                onClicked {
                    uiController.centerInMap(selectedSource.info)
                }
            })
        })

        append(detailView)

    }

    init {
        selectedSource.select(InfoID.TRACKER)
    }

    fun select(infoID: Int) {
        selectedSource.select(infoID)
    }
}
