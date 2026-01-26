package ch.bailu.aat_gtk.view.map.control

import ch.bailu.aat_gtk.config.Icons
import ch.bailu.aat_gtk.controller.OverlayControllerInterface
import ch.bailu.aat_gtk.view.menu.PopupMenuButton
import ch.bailu.aat_gtk.view.menu.provider.OverlaySelectionMenu
import ch.bailu.aat_gtk.view.preferences.SolidImageButton
import ch.bailu.aat_lib.dispatcher.TargetInterface
import ch.bailu.aat_lib.dispatcher.usage.UsageTrackerInterface
import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.edge.Position
import ch.bailu.aat_lib.map.layer.BoundingCycler
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.map.SolidPositionLock

class NavigationBar(mcontext: MapContext, storage: StorageInterface,
                    overlays: List<OverlayControllerInterface>,
                    usageTrackers: UsageTrackerInterface)
    : Bar(Position.BOTTOM), TargetInterface{

    private val boundingCycler = BoundingCycler(usageTrackers)

    init {
        add(Icons.zoomInSymbolic).onClicked { mcontext.getMapView().zoomIn() }
        add(Icons.zoomOutSymbolic).onClicked { mcontext.getMapView().zoomOut() }
        add(SolidImageButton(SolidPositionLock(storage, mcontext.getSolidKey())).button)
        add(Icons.zoomFitBestSymbolic).onClicked { boundingCycler.onAction(mcontext) }
        add(PopupMenuButton(OverlaySelectionMenu(overlays)).apply { setIcon(Icons.viewPagedSymbolic) }.menuButton)
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        boundingCycler.onContentUpdated(iid, info)
    }
}
