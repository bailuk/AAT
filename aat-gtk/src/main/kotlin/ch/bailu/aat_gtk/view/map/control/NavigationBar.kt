package ch.bailu.aat_gtk.view.map.control

import ch.bailu.aat_gtk.config.Icons
import ch.bailu.aat_gtk.controller.OverlayControllerInterface
import ch.bailu.aat_gtk.view.menu.PopupMenuButton
import ch.bailu.aat_gtk.view.menu.PopupMenuButtonOverlay
import ch.bailu.aat_gtk.view.menu.provider.OverlaySelectionMenu
import ch.bailu.aat_gtk.view.solid.SolidImageButton
import ch.bailu.aat_lib.dispatcher.TargetInterface
import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.edge.Position
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.map.SolidPositionLock
import ch.bailu.aat_lib.util.IndexedMap

class NavigationBar(mcontext: MapContext, storage: StorageInterface, overlays: List<OverlayControllerInterface>) : Bar(Position.BOTTOM),
    TargetInterface{

    private val infoCache = IndexedMap<Int, GpxInformation>()
    private var boundingCycle = 0

    init {
        add(Icons.zoomInSymbolic).onClicked { mcontext.getMapView().zoomIn() }
        add(Icons.zoomOutSymbolic).onClicked { mcontext.getMapView().zoomOut() }
        add(SolidImageButton(SolidPositionLock(storage, mcontext.getSolidKey())).button)
        add(Icons.zoomFitBestSymbolic).onClicked {
            if (nextInBoundingCycle()) {
                val info = infoCache.getValueAt(boundingCycle)

                if (info is GpxInformation) {
                    val bounding = info.getBoundingBox()
                    val fileName = info.getFile().name

                    if (fileName != null) {
                        mcontext.getMapView().frameBounding(bounding)
                        AppLog.i(this, fileName)
                    }
                }
            }
        }
        add(PopupMenuButton(OverlaySelectionMenu(overlays)).apply { setIcon(Icons.viewPagedSymbolic) }.menuButton)
    }

    private fun nextInBoundingCycle(): Boolean {
        var c: Int = infoCache.size()
        while (c > 0) {
            c--
            boundingCycle++
            if (boundingCycle >= infoCache.size()) boundingCycle = 0

            val info = infoCache.getValueAt(boundingCycle)
            val boundingBox = info?.getBoundingBox()
            val pointList = info?.getGpxList()?.pointList

            if (boundingBox != null && pointList != null && boundingBox.hasBounding() && pointList.size() > 0) {
                return true
            }
        }
        return false
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        if (info.getLoaded()) {
            infoCache.put(iid, info)
        } else {
            infoCache.remove(iid)
        }
    }
}
