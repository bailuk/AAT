package ch.bailu.aat_gtk.view

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
import ch.qos.logback.core.pattern.SpacePadder

class NavigationBar(mcontext: MapContext, storage: StorageInterface) : OnContentUpdatedInterface {
    val box: Box = Box(Orientation.HORIZONTAL, 2)
    private val plus: Button = Button.newWithLabelButton(Str("+"))
    private val minus: Button = Button.newWithLabelButton(Str("-"))
    private val lock: Button = Button.newWithLabelButton(Str("Lock"))
    private val frame: Button = Button.newWithLabelButton(Str("Frame"))
    private val grid: Button = Button.newWithLabelButton(Str("Grid"))

    private val infoCache = IndexedMap<Int, GpxInformation>()

    private var boundingCycle = 0

    init {

        plus.onClicked { mcontext.mapView.zoomIn() }
        box.packStart(plus, GTK.FALSE, GTK.FALSE, 2)

        minus.onClicked { mcontext.mapView.zoomOut() }
        box.packStart(minus, GTK.FALSE, GTK.FALSE, 0)

        lock.onClicked { SolidPositionLock(storage, mcontext.solidKey).cycle() }
        box.packStart(lock, GTK.FALSE, GTK.FALSE, 2)

        frame.onClicked {
            if (nextInBoundingCycle()) {
                mcontext.mapView.frameBounding(infoCache.getAt(boundingCycle)?.boundingBox)
                AppLog.i(infoCache.getAt(boundingCycle)?.getFile()?.name)
            }
        }
        box.packStart(frame, GTK.FALSE, GTK.FALSE, 0)

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
