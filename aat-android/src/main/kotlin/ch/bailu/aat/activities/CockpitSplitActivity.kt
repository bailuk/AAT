package ch.bailu.aat.activities

import android.os.Bundle
import android.view.View
import ch.bailu.aat_lib.dispatcher.source.SensorSource
import ch.bailu.aat.map.MapFactory
import ch.bailu.aat.map.mapsforge.MapViewLinker
import ch.bailu.aat.preferences.Storage
import ch.bailu.aat.util.AndroidTimer
import ch.bailu.aat.util.ui.AppLayout
import ch.bailu.aat.util.ui.theme.AppTheme
import ch.bailu.aat.views.layout.ContentView
import ch.bailu.aat.views.layout.PercentageLayout
import ch.bailu.aat.views.bar.ControlBar
import ch.bailu.aat.views.bar.MainControlBar
import ch.bailu.aat.views.description.CockpitView
import ch.bailu.aat.views.description.mview.MultiView
import ch.bailu.aat.views.graph.GraphViewFactory
import ch.bailu.aat_lib.description.AscendDescription
import ch.bailu.aat_lib.description.AveragePaceDescription
import ch.bailu.aat_lib.description.AverageSpeedDescriptionAP
import ch.bailu.aat_lib.description.CadenceDescription
import ch.bailu.aat_lib.description.CurrentSpeedDescription
import ch.bailu.aat_lib.description.DescendDescription
import ch.bailu.aat_lib.description.DistanceDescription
import ch.bailu.aat_lib.dispatcher.source.EditorSource
import ch.bailu.aat_lib.description.HeartRateDescription
import ch.bailu.aat_lib.description.MaximumSpeedDescription
import ch.bailu.aat_lib.description.PowerDescription
import ch.bailu.aat_lib.description.PredictiveTimeDescription
import ch.bailu.aat_lib.description.SlopeDescription
import ch.bailu.aat_lib.description.StepRateDescription
import ch.bailu.aat_lib.description.TotalStepsDescription
import ch.bailu.aat_lib.dispatcher.source.CurrentLocationSource
import ch.bailu.aat_lib.dispatcher.source.TrackerSource
import ch.bailu.aat_lib.dispatcher.source.TrackerTimerSource
import ch.bailu.aat_lib.dispatcher.source.addOverlaySources
import ch.bailu.aat_lib.dispatcher.usage.UsageTrackerAlwaysEnabled
import ch.bailu.aat_lib.dispatcher.usage.UsageTrackers
import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.gpx.information.InformationUtil

class CockpitSplitActivity : AbsKeepScreenOnActivity() {
    companion object {
        private const val SOLID_KEY = "split"
        const val SOLID_MAP_KEY = "themap"
        private val THEME = AppTheme.cockpit
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val edit = EditorSource(appContext, UsageTrackerAlwaysEnabled())
        setContentView(createContentView(edit))
        createDispatcher(edit)
    }

    private fun createContentView(edit: EditorSource): View {
        val mapSlave = MapFactory.createDefaultMapView(this, SOLID_KEY).split()
        val cockpitA = CockpitView(this, THEME)
        val cockpitB = CockpitView(this, THEME)
        val cockpitC = CockpitView(this, THEME)
        val cockpitD = CockpitView(this, THEME)
        val percentageB = PercentageLayout(this)
        percentageB.setOrientation(AppLayout.getOrientationAlongLargeSide(this))
        val percentageC = PercentageLayout(this)
        percentageC.setOrientation(AppLayout.getOrientationAlongLargeSide(this))
        val percentageD = PercentageLayout(this)
        percentageD.setOrientation(AppLayout.getOrientationAlongLargeSide(this))
        cockpitA.add(
            dispatcher, CurrentSpeedDescription(appContext.storage),
            InfoID.SPEED_SENSOR, InfoID.LOCATION
        )
        cockpitA.addC(dispatcher, AverageSpeedDescriptionAP(appContext.storage), InfoID.TRACKER)
        cockpitA.addC(dispatcher, AveragePaceDescription(appContext.storage), InfoID.TRACKER)
        cockpitA.addC(dispatcher, DistanceDescription(appContext.storage), InfoID.TRACKER)
        cockpitA.add(
            dispatcher, PredictiveTimeDescription(),
            InfoID.TRACKER_TIMER
        )
        cockpitB.addC(dispatcher, AveragePaceDescription(appContext.storage), InfoID.TRACKER)
        cockpitB.addC(dispatcher, AverageSpeedDescriptionAP(appContext.storage), InfoID.TRACKER)
        cockpitB.addC(dispatcher, MaximumSpeedDescription(appContext.storage), InfoID.TRACKER)
        percentageB.add(cockpitB, 50)
        percentageB.add(
            GraphViewFactory.createSpeedGraph(appContext, this, THEME)
                .connect(dispatcher, InfoID.TRACKER), 50
        )
        cockpitD.addAltitude(dispatcher)
        cockpitD.add(dispatcher, AscendDescription(Storage(this)), InfoID.TRACKER)
        cockpitD.add(dispatcher, DescendDescription(Storage(this)), InfoID.TRACKER)
        cockpitD.add(dispatcher, SlopeDescription(), InfoID.TRACKER)
        percentageD.add(cockpitD, 50)
        percentageD.add(
            GraphViewFactory.createAltitudeGraph(appContext, this, THEME)
                .connect(dispatcher, InfoID.TRACKER), 50
        )
        cockpitC.add(dispatcher, CadenceDescription(), InfoID.CADENCE_SENSOR)
        cockpitC.add(dispatcher, HeartRateDescription(), InfoID.HEART_RATE_SENSOR)
        cockpitC.add(dispatcher, PowerDescription(), InfoID.POWER_SENSOR)
        cockpitC.add(dispatcher, StepRateDescription(), InfoID.STEP_COUNTER_SENSOR)
        cockpitC.add(dispatcher, TotalStepsDescription(), InfoID.TRACKER)
        percentageC.add(cockpitC, 50)
        percentageC.add(
            GraphViewFactory.createSpmGraph(appContext, this, THEME)
                .connect(dispatcher, InfoID.TRACKER), 50
        )
        val mv = MultiView(this, SOLID_KEY)
        mv.add(cockpitA)
        mv.add(percentageB)
        mv.add(percentageC)
        mv.add(percentageD)
        mv.add(mapSlave)
        val mapMaster = MapFactory.createDefaultMapView(this, SOLID_MAP_KEY).map(edit, createButtonBar(mv))
        MapViewLinker(mapMaster, mapSlave)
        val contentView = ContentView(this, THEME)
        contentView.addMvIndicator(mv)
        contentView.add(
            PercentageLayout(this)
                .add(mapMaster, 70)
                .add(mv, 30)
        )
        return contentView
    }

    private fun createButtonBar(mv: MultiView): ControlBar {
        val bar = MainControlBar(this)
        bar.addActivityCycle(this)
        bar.addMvNext(mv)
        if (AppLayout.haveExtraSpaceGps(this)) {
            bar.addGpsState(this)
        }
        bar.addTrackerState(this)
        return bar
    }

    private fun createDispatcher(edit: EditorSource) {
        dispatcher.addSource(edit)
        dispatcher.addSource(TrackerSource(serviceContext, appContext.broadcaster))
        dispatcher.addSource(TrackerTimerSource(serviceContext, AndroidTimer()))
        dispatcher.addSource(CurrentLocationSource(serviceContext, appContext.broadcaster))
        dispatcher.addOverlaySources(appContext, UsageTrackers().createOverlayUsageTracker(appContext.storage, *InformationUtil.getOverlayInfoIdList().toIntArray()))
        dispatcher.addSource(SensorSource(serviceContext, appContext.broadcaster, InfoID.HEART_RATE_SENSOR))
        dispatcher.addSource(SensorSource(serviceContext, appContext.broadcaster, InfoID.POWER_SENSOR))
        dispatcher.addSource(SensorSource(serviceContext, appContext.broadcaster, InfoID.CADENCE_SENSOR))
        dispatcher.addSource(SensorSource(serviceContext, appContext.broadcaster, InfoID.SPEED_SENSOR))
        dispatcher.addSource(SensorSource(serviceContext, appContext.broadcaster, InfoID.STEP_COUNTER_SENSOR))
    }
}
