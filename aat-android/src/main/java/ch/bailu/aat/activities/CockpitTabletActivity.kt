package ch.bailu.aat.activities

import android.os.Bundle
import android.view.View
import ch.bailu.aat.dispatcher.SensorSource
import ch.bailu.aat.map.MapFactory
import ch.bailu.aat.map.mapsforge.MapViewLinker
import ch.bailu.aat.util.AndroidTimer
import ch.bailu.aat.util.ui.AppLayout
import ch.bailu.aat.util.ui.AppTheme
import ch.bailu.aat.views.ContentView
import ch.bailu.aat.views.PercentageLayout
import ch.bailu.aat.views.bar.ControlBar
import ch.bailu.aat.views.bar.MainControlBar
import ch.bailu.aat.views.description.CockpitView
import ch.bailu.aat.views.graph.GraphViewFactory
import ch.bailu.aat_lib.description.AltitudeDescription
import ch.bailu.aat_lib.description.AverageSpeedDescription
import ch.bailu.aat_lib.description.CadenceDescription
import ch.bailu.aat_lib.description.CurrentSpeedDescription
import ch.bailu.aat_lib.description.DistanceDescription
import ch.bailu.aat_lib.description.EditorSource
import ch.bailu.aat_lib.description.HeartRateDescription
import ch.bailu.aat_lib.description.MaximumSpeedDescription
import ch.bailu.aat_lib.description.PredictiveTimeDescription
import ch.bailu.aat_lib.dispatcher.CurrentLocationSource
import ch.bailu.aat_lib.dispatcher.EditorSourceInterface
import ch.bailu.aat_lib.dispatcher.OverlaysSource
import ch.bailu.aat_lib.dispatcher.TrackerSource
import ch.bailu.aat_lib.dispatcher.TrackerTimerSource
import ch.bailu.aat_lib.gpx.InfoID

class CockpitTabletActivity : AbsKeepScreenOnActivity() {
    private val theme = AppTheme.cockpit

    companion object {
        private const val SOLID_KEY = "cockpit_tablet"
        private const val SOLID_MAP_KEY = SOLID_KEY + "_map"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val edit = EditorSource(appContext)
        setContentView(createContentView(edit))
        createDispatcher(edit)
    }

    private fun createContentView(edit: EditorSourceInterface): View {
        val result = ContentView(this, theme)
        val smallMap = MapFactory.DEF(this, SOLID_KEY).split()
        val bigMap = MapFactory.DEF(this, SOLID_MAP_KEY).map(edit, createButtonBar())
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
        allComponents.add(GraphViewFactory.all(appContext, this, this, theme, InfoID.TRACKER), 20)

        result.add(errorView)
        result.add(allComponents)
        return result
    }

    private fun createCockpit(): CockpitView {
        return CockpitView(this, theme).apply {
            add(this@CockpitTabletActivity, CurrentSpeedDescription(appContext.storage), InfoID.SPEED_SENSOR, InfoID.LOCATION)
            addC(this@CockpitTabletActivity, AverageSpeedDescription(appContext.storage), InfoID.TRACKER)
            add(this@CockpitTabletActivity, CadenceDescription(), InfoID.CADENCE_SENSOR)
            add(this@CockpitTabletActivity, PredictiveTimeDescription(), InfoID.TRACKER_TIMER)
            addC(this@CockpitTabletActivity, DistanceDescription(appContext.storage), InfoID.TRACKER)
            add(this@CockpitTabletActivity, AltitudeDescription(appContext.storage), InfoID.LOCATION)
            add(this@CockpitTabletActivity, MaximumSpeedDescription(appContext.storage), InfoID.TRACKER)
            add(this@CockpitTabletActivity, HeartRateDescription(), InfoID.HEART_RATE_SENSOR)
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
        addSource(edit)
        addSource(TrackerSource(serviceContext, appContext.broadcaster))
        addSource(TrackerTimerSource(serviceContext, AndroidTimer()))
        addSource(CurrentLocationSource(serviceContext, appContext.broadcaster))
        addSource(OverlaysSource(appContext))
        addSource(SensorSource(serviceContext, InfoID.HEART_RATE_SENSOR))
        addSource(SensorSource(serviceContext, InfoID.CADENCE_SENSOR))
        addSource(SensorSource(serviceContext, InfoID.SPEED_SENSOR))
    }
}
