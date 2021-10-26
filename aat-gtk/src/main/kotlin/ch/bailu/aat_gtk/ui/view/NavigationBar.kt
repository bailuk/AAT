package ch.bailu.aat_gtk.ui.view

import ch.bailu.aat_gtk.ui.util.IconMap
import ch.bailu.aat_gtk.util.IndexedMap
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.map.SolidMapGrid
import ch.bailu.aat_lib.preferences.map.SolidPositionLock
import ch.bailu.gtk.GTK
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Button
import ch.bailu.gtk.gtk.Orientation
import ch.bailu.gtk.type.Str

class NavigationBar(mcontext: MapContext, storage: StorageInterface) : OnContentUpdatedInterface {
    val SIZE = 30
    
    val box: Box = Box(Orientation.HORIZONTAL, 2)
    private val plus: Button = Button()
    private val minus: Button = Button()
    private val lock: Button = Button()
    private val frame: Button = Button()
    private val grid: Button = Button()

    private val infoCache = IndexedMap<Int, GpxInformation>()

    private var boundingCycle = 0

    init {
        plus.image = IconMap.getImage("zoom-in-symbolic", SIZE)
        plus.onClicked { mcontext.mapView.zoomIn() }
        box.packStart(plus, GTK.FALSE, GTK.FALSE, 2)

        minus.image = IconMap.getImage("zoom-out-symbolic", SIZE)
        minus.onClicked { mcontext.mapView.zoomOut() }
        box.packStart(minus, GTK.FALSE, GTK.FALSE, 0)

        lock.image = IconMap.getImage("zoom-original-symbolic", SIZE)
        lock.onClicked { SolidPositionLock(storage, mcontext.solidKey).cycle() }
        box.packStart(lock, GTK.FALSE, GTK.FALSE, 2)

        frame.image = IconMap.getImage("zoom-fit-best-symbolic", SIZE)
        frame.onClicked {
            if (nextInBoundingCycle()) {
                mcontext.mapView.frameBounding(infoCache.getAt(boundingCycle)?.boundingBox)
                AppLog.i(infoCache.getAt(boundingCycle)?.getFile()?.name)
            }
        }
        box.packStart(frame, GTK.FALSE, GTK.FALSE, 0)

        grid.image = IconMap.getImage("view-grid-symbolic", SIZE)
        grid.onClicked { SolidMapGrid(storage, mcontext.solidKey).cycle() }
        box.packStart(grid, GTK.FALSE, GTK.FALSE, 2)
    }

    private fun nextInBoundingCycle(): Boolean {
        var c: Int = infoCache.size()
        while (c > 0) {
            c--
            boundingCycle++
            if (boundingCycle >= infoCache.size()) boundingCycle = 0

            val info = infoCache.getAt(boundingCycle)
            val boundingBox = info?.boundingBox
            val pointList = info?.gpxList?.pointList

            if (boundingBox != null && pointList != null) {
                return boundingBox.hasBounding() && pointList.size() > 0;
            }
        }
        return false
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        if (info.isLoaded) {
            infoCache.put(iid, info)
        } else {
            infoCache.remove(iid)
        }
    }
}
