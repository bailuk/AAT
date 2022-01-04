package ch.bailu.aat_gtk.ui.view

import ch.bailu.aat_gtk.ui.util.IconMap
import ch.bailu.aat_gtk.ui.view.solid.SolidImageButton
import ch.bailu.aat_gtk.ui.view.solid.SolidMenuButton
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.map.SolidMapGrid
import ch.bailu.aat_lib.preferences.map.SolidPositionLock
import ch.bailu.aat_lib.util.IndexedMap
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Button
import ch.bailu.gtk.gtk.Orientation
import ch.bailu.gtk.helper.ActionHelper

class NavigationBar(actionHelper: ActionHelper, mcontext: MapContext, storage: StorageInterface) : OnContentUpdatedInterface {
    val SIZE = 24
    
    val box = Box(Orientation.HORIZONTAL, 2)
    private val plus: Button = Button()
    private val minus: Button = Button()
    private val frame: Button = Button()
    private val lock = SolidImageButton(SolidPositionLock(storage, mcontext.solidKey))
    private val grid = SolidMenuButton(actionHelper, SolidMapGrid(storage, mcontext.solidKey))

    private val infoCache = IndexedMap<Int, GpxInformation>()

    private var boundingCycle = 0

    init {


        plus.child = IconMap.getImage("zoom-in-symbolic", SIZE)
        plus.onClicked { mcontext.mapView.zoomIn() }
        box.append(plus)

        minus.child = IconMap.getImage("zoom-out-symbolic", SIZE)
        minus.onClicked { mcontext.mapView.zoomOut() }
        box.append(minus)

        box.append(lock.button)

        frame.child = IconMap.getImage("zoom-fit-best-symbolic", SIZE)
        frame.onClicked {
            if (nextInBoundingCycle()) {
                mcontext.mapView.frameBounding(infoCache.getAt(boundingCycle)?.boundingBox)
                AppLog.i(infoCache.getAt(boundingCycle)?.getFile()?.name)
            }
        }
        box.append(frame)

        box.append(grid.button)
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
