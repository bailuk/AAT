package ch.bailu.aat_gtk.view.map.control

import ch.bailu.aat_gtk.config.Icons
import ch.bailu.aat_gtk.view.UiController
import ch.bailu.aat_gtk.view.menu.PopupButton
import ch.bailu.aat_gtk.view.menu.provider.OverlaySelectionMenu
import ch.bailu.aat_gtk.view.solid.SolidImageButton
import ch.bailu.aat_lib.dispatcher.GpxInformationSource
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface
import ch.bailu.aat_lib.gpx.GpxInformationCache
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.edge.Position
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.map.SolidPositionLock

class NavigationBar(mcontext: MapContext, storage: StorageInterface, overlays: List<GpxInformationSource>, uiController: UiController) : Bar(Position.BOTTOM),
    OnContentUpdatedInterface{

    private val infoCache = GpxInformationCache()
    private var boundingCycle = 0

    init {
        add(Icons.zoomInSymbolic).onClicked { mcontext.getMapView().zoomIn() }
        add(Icons.zoomOutSymbolic).onClicked { mcontext.getMapView().zoomOut() }
        add(SolidImageButton(SolidPositionLock(storage, mcontext.getSolidKey())).button)
        add(Icons.zoomFitBestSymbolic).onClicked {
            if (nextInBoundingCycle()) {
                val info = infoCache.getValueAt(boundingCycle)

                 val bounding = info.getBoundingBox()
                 val fileName = info.getFile().name

                 if (fileName != null) {
                     mcontext.getMapView().frameBounding(bounding)
                     AppLog.i(this, fileName)
                 }
            }
        }
        add(PopupButton(OverlaySelectionMenu(overlays, uiController)).apply { setIcon(Icons.viewPagedSymbolic) }.overlay)
    }

    private fun nextInBoundingCycle(): Boolean {
        var c: Int = infoCache.size()
        while (c > 0) {
            c--
            boundingCycle++
            if (boundingCycle >= infoCache.size()) boundingCycle = 0

            val info = infoCache.getValueAt(boundingCycle)
            val boundingBox = info.getBoundingBox()
            val pointList = info.getGpxList().pointList

            if (boundingBox.hasBounding() && pointList.size() > 0) {
                return true
            }
        }
        return false
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        infoCache.onContentUpdated(iid, info)
    }
}
