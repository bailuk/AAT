package ch.bailu.aat.map

import android.content.Context
import ch.bailu.aat.activities.ActivityContext
import ch.bailu.aat.map.layer.control.CustomBarLayer
import ch.bailu.aat.map.layer.control.EditorBarLayer
import ch.bailu.aat.map.layer.control.InformationBarLayer
import ch.bailu.aat.map.layer.control.NavigationBarLayer
import ch.bailu.aat.map.mapsforge.MapsForgeView
import ch.bailu.aat.map.mapsforge.MapsForgeViewBase
import ch.bailu.aat.util.ui.AppTheme
import ch.bailu.aat.views.bar.ControlBar
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.dispatcher.DispatcherInterface
import ch.bailu.aat_lib.dispatcher.EditorSourceInterface
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.layer.gpx.GpxDynLayer
import ch.bailu.aat_lib.map.layer.gpx.GpxOverlayListLayer
import ch.bailu.aat_lib.map.layer.grid.Crosshair
import ch.bailu.aat_lib.map.layer.grid.GridDynLayer
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.location.CurrentLocationLayer
import ch.bailu.aat_lib.service.ServicesInterface

class MapFactory(private val m: MapsForgeViewBase, activityContext: ActivityContext) {
    private val d: DispatcherInterface = activityContext
    private val mc: MapContext = m.mContext
    private val s: StorageInterface = activityContext.appContext.storage
    private val ser: ServicesInterface = activityContext.serviceContext
    private val appContext: AppContext= activityContext.appContext
    private val c: Context = activityContext

    init {
        activityContext.addLifeCycle(m)
    }

    private fun base() {
        m.add(CurrentLocationLayer(mc, d))
        m.add(NavigationBarLayer(c, mc, d, 4))
    }

    fun split(): MapsForgeViewBase {
        m.add(CurrentLocationLayer(mc, d))
        m.add(GpxOverlayListLayer(s, mc, ser, d))
        m.add(GpxDynLayer(s, mc, ser, d, InfoID.EDITOR_DRAFT))
        m.add(GpxDynLayer(s, mc, ser, d, InfoID.TRACKER))
        m.add(Crosshair())
        return m
    }

    fun tracker(e: EditorSourceInterface): MapsForgeViewBase {
        return tracker(e, InfoID.EDITOR_DRAFT)
    }

    private fun tracker(e: EditorSourceInterface, iid: Int): MapsForgeViewBase {
        base()
        m.add(GpxOverlayListLayer(s, mc, ser, d))
        m.add(EditorBarLayer(appContext, c, mc, d, iid, e))
        m.add(GpxDynLayer(s, mc, ser, d, InfoID.FILEVIEW))
        m.add(GpxDynLayer(s, mc, ser, d, InfoID.TRACKER))
        m.add(GridDynLayer(ser, s, mc))
        m.add(InformationBarLayer(appContext, c, mc, d))
        return m
    }

    fun map(e: EditorSourceInterface, b: ControlBar?): MapsForgeViewBase {
        tracker(e)
        m.add(CustomBarLayer(mc, b, AppTheme.bar))
        return m
    }

    fun list(): MapsForgeViewBase {
        base()
        m.add(GpxOverlayListLayer(s, mc, ser, d))
        m.add(GpxDynLayer(s, mc, ser, d, InfoID.LIST_SUMMARY))
        m.add(GridDynLayer(ser, s, mc))
        m.add(InformationBarLayer(appContext, c, mc, d))
        return m
    }

    fun editor(e: EditorSourceInterface): MapsForgeViewBase {
        return tracker(e, InfoID.EDITOR_OVERLAY)
    }

    fun content(e: EditorSourceInterface): MapsForgeViewBase {
        return editor(e)
    }

    fun node(): MapsForgeViewBase {
        base()
        m.add(GpxDynLayer(s, mc, ser, d, InfoID.TRACKER))
        m.add(GpxDynLayer(s, mc, ser, d, InfoID.FILEVIEW))
        m.add(GridDynLayer(ser, s, mc))
        return m
    }

    fun externalContent(): MapsForgeViewBase {
        m.add(GpxOverlayListLayer(s, mc, ser, d))
        m.add(GpxDynLayer(s, mc, ser, d, InfoID.FILEVIEW))
        m.add(CurrentLocationLayer(mc, d))
        m.add(GridDynLayer(ser, s, mc))
        m.add(NavigationBarLayer(c, mc, d))
        m.add(InformationBarLayer(appContext, c, mc, d))
        return m
    }

    companion object {
        fun DEF(activityContext: ActivityContext, skey: String): MapFactory {
            return MF(activityContext, skey)
        }

        fun MF(activityContext: ActivityContext, skey: String): MapFactory {
            return MapFactory(
                MapsForgeView(
                    activityContext,
                    activityContext.appContext,
                    activityContext,
                    skey
                ), activityContext
            )
        }
    }
}
