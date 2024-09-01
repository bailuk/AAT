package ch.bailu.aat_gtk.view.toplevel

import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_gtk.config.Icons
import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_gtk.controller.OverlayController
import ch.bailu.aat_gtk.controller.UiControllerInterface
import ch.bailu.aat_gtk.lib.extensions.margin
import ch.bailu.aat_gtk.view.menu.PopupMenuButton
import ch.bailu.aat_gtk.view.menu.provider.OverlaySelectionMenu
import ch.bailu.aat_lib.dispatcher.Dispatcher
import ch.bailu.aat_lib.dispatcher.usage.SelectableUsageTracker
import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.gpx.information.InformationUtil
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Button
import ch.bailu.gtk.gtk.Label
import ch.bailu.gtk.gtk.Orientation

class DetailViewPage(
    uiController: UiControllerInterface,
    dispatcher: Dispatcher,
    private val usageTracker: SelectableUsageTracker
) {

    private val detailView = DetailView(dispatcher, usageTracker, GtkAppContext.storage).scrolled
    private val selectedLabel = Label("").apply { addCssClass("page-label") }

    val box = Box(Orientation.VERTICAL, 0).apply {
        append(Box(Orientation.HORIZONTAL, Layout.margin).apply {
            margin(Layout.margin)

            append(Box(Orientation.HORIZONTAL, 0).apply {
                addCssClass(Strings.linked)

                append(Button().apply {
                    iconName = Icons.zoomFitBestSymbolic
                    onClicked {
                        uiController.showMap()
                        uiController.frameInMap(usageTracker.getIID())
                    }
                })

                append(Button().apply {
                    iconName = Icons.findLocationSymbolic
                    onClicked {
                        uiController.showMap()
                        uiController.centerInMap(usageTracker.getIID())
                    }
                }
                )

                val overlayControllers =
                    OverlayController.createMapOverlayControllers(GtkAppContext.storage, uiController)
                append(PopupMenuButton(OverlaySelectionMenu(overlayControllers)).apply { setIcon(Icons.viewPagedSymbolic) }.menuButton)

            })

            append(selectedLabel)
        })

        append(detailView)
    }

    init {
        select(InfoID.TRACKER)
    }

    fun select(iid: Int) {
        selectedLabel.setLabel(InformationUtil.defaultName(iid))
        usageTracker.select(iid)
    }
}
