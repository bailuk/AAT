package ch.bailu.aat.activities

import android.os.Bundle
import android.view.View
import ch.bailu.aat_lib.dispatcher.source.SensorSource
import ch.bailu.aat.map.MapFactory
import ch.bailu.aat.map.mapsforge.MapViewLinker
import ch.bailu.aat.util.AndroidTimer
import ch.bailu.aat.util.ui.AppLayout
import ch.bailu.aat.util.ui.theme.AppTheme
import ch.bailu.aat.views.layout.ContentView
import ch.bailu.aat.views.layout.PercentageLayout
import ch.bailu.aat.views.bar.ControlBar
import ch.bailu.aat.views.bar.MainControlBar
import ch.bailu.aat.views.description.CockpitView
import ch.bailu.aat.views.graph.GraphViewFactory
import ch.bailu.aat_lib.description.AltitudeDescription
import ch.bailu.aat_lib.description.AverageSpeedDescription
import ch.bailu.aat_lib.description.CadenceDescription
import ch.bailu.aat_lib.description.CurrentSpeedDescription
import ch.bailu.aat_lib.description.DistanceDescription
import ch.bailu.aat_lib.dispatcher.source.EditorSource
import ch.bailu.aat_lib.description.HeartRateDescription
import ch.bailu.aat_lib.description.MaximumSpeedDescription
import ch.bailu.aat_lib.description.PredictiveTimeDescription
import ch.bailu.aat_lib.dispatcher.source.CurrentLocationSource
import ch.bailu.aat_lib.dispatcher.EditorSourceInterface
import ch.bailu.aat_lib.dispatcher.source.TrackerSource
import ch.bailu.aat_lib.dispatcher.source.TrackerTimerSource
import ch.bailu.aat_lib.dispatcher.source.addOverlaySources
import ch.bailu.aat_lib.dispatcher.usage.UsageTrackerAlwaysEnabled
import ch.bailu.aat_lib.dispatcher.usage.UsageTrackers
import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.gpx.information.InformationUtil

class CockpitTabletActivity : AbsKeepScreenOnActivity() {
    private val theme = AppTheme.cockpit

    companion object {
        private const val SOLID_KEY = "cockpit_tablet"
        private const val SOLID_MAP_KEY = SOLID_KEY + "_map"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val edit = EditorSource(appContext, UsageTrackerAlwaysEnabled())
        setContentView(createContentView(edit))
        createDispatcher(edit)
    }

    private fun createContentView(edit: EditorSourceInterface): View {
        val result = ContentView(this, theme)
        val smallMap = MapFactory.createDefaultMapView(this, SOLID_KEY).split()
        val bigMap = MapFactory.createDefaultMapView(this, SOLID_MAP_KEY).map(edit, createButtonBar())
        MapViewLinker(bigMap, smallMap)

        val cockpitAndSmallMap = PercentageLayout(this)
        cockpitAndSmallMap.setOrientation(AppLayout.getOrientationAlongSmallSide(this))
        cockpitAndSmallMap.add(createCockpit(), 60)
        cockpitAndSmallMap.add(smallMap.toView(), 40)

        val cockpitAndBigMap = PercentageLayout(this)
        cockpitAndBigMap.setOrientation(AppLayout.getOrientationAlongLargeSide(this))
        cockpitAndBigMap.add(bigMap, 60)
        cockpitAndBigMap.add(cockpitAndSmallMap, 40)

        val allComponents = PercentageLayout(this)
        allComponents.add(cockpitAndBigMap, 80)
        allComponents.add(GraphViewFactory.all(appContext, this, dispatcher, theme, InfoID.TRACKER), 20)

        result.add(allComponents)
        return result
    }

    private fun createCockpit(): CockpitView {
        return CockpitView(this, theme).apply {
            add(dispatcher, CurrentSpeedDescription(appContext.storage), InfoID.SPEED_SENSOR, InfoID.LOCATION)
            addC(dispatcher, AverageSpeedDescription(appContext.storage), InfoID.TRACKER)
            add(dispatcher, CadenceDescription(), InfoID.CADENCE_SENSOR)
            add(dispatcher, PredictiveTimeDescription(), InfoID.TRACKER_TIMER)
            addC(dispatcher, DistanceDescription(appContext.storage), InfoID.TRACKER)
            add(dispatcher, AltitudeDescription(appContext.storage), InfoID.LOCATION)
            add(dispatcher, MaximumSpeedDescription(appContext.storage), InfoID.TRACKER)
            add(dispatcher, HeartRateDescription(), InfoID.HEART_RATE_SENSOR)
        }
    }

    private fun createButtonBar(): ControlBar {
        return MainControlBar(this).apply {
            addActivityCycle(this@CockpitTabletActivity)
            addSpace()
            if (AppLayout.haveExtraSpaceGps(this@CockpitTabletActivity)) {
                addGpsState(this@CockpitTabletActivity)
            }
            addTrackerState(this@CockpitTabletActivity)
        }
    }

    private fun createDispatcher(edit: EditorSource) {
        dispatcher.addSource(edit)
        dispatcher.addSource(TrackerSource(serviceContext, appContext.broadcaster))
        dispatcher.addSource(TrackerTimerSource(serviceContext, AndroidTimer()))
        dispatcher.addSource(CurrentLocationSource(serviceContext, appContext.broadcaster))
        dispatcher.addOverlaySources(appContext, UsageTrackers().createOverlayUsageTracker(appContext.storage, *InformationUtil.getOverlayInfoIdList().toIntArray()))
        dispatcher.addSource(SensorSource(serviceContext, appContext.broadcaster, InfoID.HEART_RATE_SENSOR))
        dispatcher.addSource(SensorSource(serviceContext, appContext.broadcaster, InfoID.CADENCE_SENSOR))
        dispatcher.addSource(SensorSource(serviceContext, appContext.broadcaster, InfoID.SPEED_SENSOR))
    }
}
