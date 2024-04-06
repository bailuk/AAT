package ch.bailu.aat.map.layer

import android.content.Context
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import ch.bailu.aat.R
import ch.bailu.aat.activities.AbsHardwareButtons
import ch.bailu.aat.map.To
import ch.bailu.aat.preferences.Storage
import ch.bailu.aat.util.ui.AppLayout
import ch.bailu.aat.util.ui.theme.AppTheme
import ch.bailu.aat.util.ui.tooltip.ToolTip
import ch.bailu.aat.views.bar.ControlBar
import ch.bailu.aat_lib.dispatcher.DispatcherInterface
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.edge.Position
import ch.bailu.aat_lib.preferences.map.SolidPositionLock
import ch.bailu.aat_lib.util.IndexedMap

class NavigationBarLayer @JvmOverloads constructor(context: Context, private val mcontext: MapContext, d: DispatcherInterface, i: Int = AppLayout.DEFAULT_VISIBLE_BUTTON_COUNT) : ControlBarLayer(
    mcontext, ControlBar(context, getOrientation(Position.BOTTOM), AppTheme.bar, i), Position.BOTTOM
), OnContentUpdatedInterface {

    private val buttonPlus: View
    private val buttonMinus: View
    private val buttonFrame: View
    private val infoCache = IndexedMap<Int, GpxInformation>()
    private var boundingCycle = 0

    init {
        buttonPlus = bar.addImageButton(R.drawable.zoom_in)
        buttonMinus = bar.addImageButton(R.drawable.zoom_out)
        val lock = bar.addSolidIndexButton(
            SolidPositionLock(Storage(context), mcontext.getSolidKey())
        )
        buttonFrame = bar.addImageButton(R.drawable.zoom_fit_best)
        ToolTip.set(buttonPlus, R.string.tt_map_zoomin)
        ToolTip.set(buttonMinus, R.string.tt_map_zoomout)
        ToolTip.set(buttonFrame, R.string.tt_map_frame)
        ToolTip.set(lock, R.string.tt_map_home)
        d.addTarget(this, InfoID.ALL)
        val volumeView = VolumeView(context)
        volumeView.visibility = View.INVISIBLE
        To.view(mcontext.getMapView())!!.addView(volumeView)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        if (v === buttonPlus) {
            mcontext.getMapView().zoomIn()
        } else if (v === buttonMinus) {
            mcontext.getMapView().zoomOut()
        } else if (v === buttonFrame && infoCache.size() > 0) {
            if (nextInBoundingCycle()) {
                val info = infoCache.getValueAt(boundingCycle)

                if (info is GpxInformation) {
                    mcontext.getMapView().frameBounding(info.getBoundingBox())
                    AppLog.i(v.context, info.file.name)
                }
            }
        }
    }

    private fun nextInBoundingCycle(): Boolean {
        var c = infoCache.size()
        while (c > 0) {
            c--
            boundingCycle++
            if (boundingCycle >= infoCache.size()) boundingCycle = 0

            val info = infoCache.getValueAt(boundingCycle)
            if (info is GpxInformation) {
                if (info.getBoundingBox().hasBounding()
                    || info.gpxList.pointList.size() > 0
                ) return true
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

    override fun drawInside(mcontext: MapContext) {}
    override fun onAttached() {}
    override fun onDetached() {}

    private inner class VolumeView(context: Context) : ViewGroup(context),
        AbsHardwareButtons.OnHardwareButtonPressed {

        override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {}

        override fun onHardwareButtonPressed(code: Int, type: AbsHardwareButtons.EventType): Boolean {
            if (To.view(mcontext.getMapView())!!.visibility == VISIBLE) {
                if (code == KeyEvent.KEYCODE_VOLUME_UP) {
                    if (type === AbsHardwareButtons.EventType.DOWN) mcontext.getMapView().zoomIn()
                    return true
                }
                if (code == KeyEvent.KEYCODE_VOLUME_DOWN) {
                    if (type === AbsHardwareButtons.EventType.DOWN) mcontext.getMapView().zoomOut()
                    return true
                }
            }
            return false
        }
    }
}
