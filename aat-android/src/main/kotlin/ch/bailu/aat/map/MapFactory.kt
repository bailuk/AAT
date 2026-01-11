package ch.bailu.aat.map

import android.content.Context
import ch.bailu.aat.activities.ActivityContext
import ch.bailu.aat.app.AndroidInformationUtil
import ch.bailu.aat.map.layer.CustomBarLayer
import ch.bailu.aat.map.layer.EditorBarLayer
import ch.bailu.aat.map.layer.InformationBarLayer
import ch.bailu.aat.map.layer.NavigationBarLayer
import ch.bailu.aat.map.mapsforge.MapsForgeView
import ch.bailu.aat.map.mapsforge.MapsForgeViewBase
import ch.bailu.aat.util.ui.theme.AppTheme
import ch.bailu.aat.views.bar.ControlBar
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.dispatcher.DispatcherInterface
import ch.bailu.aat_lib.dispatcher.EditorSourceInterface
import ch.bailu.aat_lib.dispatcher.filter.ToggleFilter
import ch.bailu.aat_lib.dispatcher.usage.UsageTrackerInterface
import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.layer.gpx.GpxDynLayer
import ch.bailu.aat_lib.map.layer.grid.Crosshair
import ch.bailu.aat_lib.map.layer.grid.GridDynLayer
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.location.CurrentLocationLayer
import ch.bailu.aat_lib.service.ServicesInterface

class MapFactory(private val m: MapsForgeViewBase, activityContext: ActivityContext) {
    private val d: DispatcherInterface = activityContext.dispatcher
    private val mc: MapContext = m.getMContext()
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

    fun split(usageTracker: UsageTrackerInterface): MapsForgeViewBase {
        m.add(CurrentLocationLayer(mc, d))
        addMapOverlays(usageTracker)
        m.add(GpxDynLayer(s, mc, ser, d, InfoID.EDITOR_DRAFT))
        m.add(GpxDynLayer(s, mc, ser, d, InfoID.TRACKER))
        m.add(Crosshair())
        return m
    }

    fun tracker(e: EditorSourceInterface, usageTracker: UsageTrackerInterface): MapsForgeViewBase {
        return tracker(e, InfoID.EDITOR_DRAFT, usageTracker)
    }

    private fun tracker(e: EditorSourceInterface, iid: Int, usageTracker: UsageTrackerInterface): MapsForgeViewBase {
        base()

        addMapOverlays(usageTracker)

        m.add(EditorBarLayer(appContext, c, mc, d, iid, e))
        m.add(GpxDynLayer(s, mc, ser, d, InfoID.FILE_VIEW))
        m.add(GpxDynLayer(s, mc, ser, d, InfoID.TRACKER))

        m.add(GridDynLayer(ser, s, mc))
        m.add(InformationBarLayer(appContext, c, mc, d))
        return m
    }

    private fun addMapOverlays(usageTracker: UsageTrackerInterface) {
        AndroidInformationUtil.mapOverlayInfoIdList.forEach { infoID ->
            val layer = GpxDynLayer(appContext.storage, m.getMContext(), appContext.services)
            d.addTarget(ToggleFilter(layer, infoID, usageTracker))
            m.add(layer)
        }
    }

    fun map(e: EditorSourceInterface, b: ControlBar, usageTracker: UsageTrackerInterface): MapsForgeViewBase {
        tracker(e, usageTracker)
        m.add(CustomBarLayer(mc, b, AppTheme.bar))
        return m
    }

    fun list(usageTracker: UsageTrackerInterface): MapsForgeViewBase {
        base()
        addMapOverlays(usageTracker)
        m.add(GpxDynLayer(s, mc, ser, d, InfoID.LIST_SUMMARY))
        m.add(GridDynLayer(ser, s, mc))
        m.add(InformationBarLayer(appContext, c, mc, d))
        return m
    }

    fun editor(e: EditorSourceInterface, usageTracker: UsageTrackerInterface): MapsForgeViewBase {
        return tracker(e, InfoID.EDITOR_OVERLAY, usageTracker)
    }

    fun content(e: EditorSourceInterface, usageTracker: UsageTrackerInterface): MapsForgeViewBase {
        return editor(e, usageTracker)
    }

    fun node(): MapsForgeViewBase {
        base()
        m.add(GpxDynLayer(s, mc, ser, d, InfoID.TRACKER))
        m.add(GpxDynLayer(s, mc, ser, d, InfoID.FILE_VIEW))
        m.add(GridDynLayer(ser, s, mc))
        return m
    }

    fun externalContent(usageTracker: UsageTrackerInterface): MapsForgeViewBase {
        addMapOverlays(usageTracker)
        m.add(GpxDynLayer(s, mc, ser, d, InfoID.FILE_VIEW))
        m.add(CurrentLocationLayer(mc, d))
        m.add(GridDynLayer(ser, s, mc))
        m.add(NavigationBarLayer(c, mc, d))
        m.add(InformationBarLayer(appContext, c, mc, d))
        return m
    }

    companion object {
        fun createDefaultMapView(activityContext: ActivityContext, skey: String): MapFactory {
            return createMultiViewMapView(activityContext, skey)
        }

        private fun createMultiViewMapView(activityContext: ActivityContext, skey: String): MapFactory {
            return MapFactory(
                MapsForgeView(
                    activityContext,
                    activityContext.appContext,
                    activityContext.dispatcher,
                    skey
                ), activityContext
            )
        }
    }
}
