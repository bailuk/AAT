package ch.bailu.aat_gtk.view.toplevel

import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_gtk.config.Icons
import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_gtk.controller.OverlayController
import ch.bailu.aat_gtk.controller.UiControllerInterface
import ch.bailu.aat_gtk.util.extensions.margin
import ch.bailu.aat_gtk.view.menu.PopupMenuButton
import ch.bailu.aat_gtk.view.menu.provider.FileMenu
import ch.bailu.aat_gtk.view.menu.provider.OverlaySelectionMenu
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.dispatcher.Dispatcher
import ch.bailu.aat_lib.dispatcher.TargetInterface
import ch.bailu.aat_lib.dispatcher.filter.SelectFilter
import ch.bailu.aat_lib.dispatcher.usage.SelectableUsageTracker
import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.gpx.information.InformationUtil
import ch.bailu.gtk.adw.Application
import ch.bailu.gtk.gdk.Display
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Button
import ch.bailu.gtk.gtk.Label
import ch.bailu.gtk.gtk.Orientation

class DetailViewPage(
    app: Application,
    display: Display,
    appContext: AppContext,
    uiController: UiControllerInterface,
    dispatcher: Dispatcher,
    private val usageTracker: SelectableUsageTracker
): TargetInterface {

    private val detailView = DetailView(dispatcher, usageTracker, GtkAppContext.storage).scrolled
    private val selectedLabel = Label("").apply { addCssClass("page-label") }

    private val fileMenu = FileMenu(appContext, display)
    private val fileMenuButton = PopupMenuButton(fileMenu).apply { createActions(app) }.menuButton
    init {
        dispatcher.addTarget(
            SelectFilter(this, usageTracker),
            *InformationUtil.getMapOverlayInfoIdList().toIntArray()
        )
    }

    val box = Box(Orientation.VERTICAL, 0).apply {
        append(Box(Orientation.HORIZONTAL, Layout.MARGIN).apply {
            margin(Layout.MARGIN)

            append(Box(Orientation.HORIZONTAL, 0).apply {
                addCssClass(Strings.CSS_LINKED)

                append(Button().apply {
                    iconName = Icons.zoomFitBestSymbolic
                    onClicked {
                        uiController.showMap()
                        uiController.frameInMap(usageTracker.getIID())
                        uiController.setOverlayEnabled(usageTracker.getIID(), true)
                    }
                })

                append(Button().apply {
                    iconName = Icons.findLocationSymbolic
                    onClicked {
                        uiController.showMap()
                        uiController.centerInMap(usageTracker.getIID())
                        uiController.setOverlayEnabled(usageTracker.getIID(), true)
                    }
                })

                val overlayControllers =
                    OverlayController.createMapOverlayControllers(GtkAppContext.storage, uiController)
                append(PopupMenuButton(OverlaySelectionMenu(overlayControllers)).apply { setIcon(Icons.viewPagedSymbolic) }.menuButton)
                append(fileMenuButton)

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

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        val file = info.getFile()
        fileMenu.setFile(file)
        fileMenuButton.sensitive = file.name.isNotEmpty() && InformationUtil.supportsFileOperations(iid)
    }
}
