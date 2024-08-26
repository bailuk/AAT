package ch.bailu.aat.activities

import android.os.Bundle
import android.view.View
import ch.bailu.aat_lib.dispatcher.source.SensorSource
import ch.bailu.aat.map.MapFactory
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
import ch.bailu.aat_lib.description.AverageSpeedDescriptionAP
import ch.bailu.aat_lib.description.CurrentSpeedDescription
import ch.bailu.aat_lib.description.DistanceDescription
import ch.bailu.aat_lib.dispatcher.EditorSource
import ch.bailu.aat_lib.description.MaximumSpeedDescription
import ch.bailu.aat_lib.description.PredictiveTimeDescription
import ch.bailu.aat_lib.dispatcher.source.CurrentLocationSource
import ch.bailu.aat_lib.dispatcher.source.TrackerSource
import ch.bailu.aat_lib.dispatcher.source.TrackerTimerSource
import ch.bailu.aat_lib.gpx.InfoID

class CockpitActivity : AbsKeepScreenOnActivity() {
    companion object {
        const val SOLID_KEY = "tracker"
    }

    private val theme = AppTheme.cockpit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val edit = EditorSource(appContext)
        val contentView = ContentView(this, theme)
        val multiView = createMultiView(edit)
        contentView.addMvIndicator(multiView)
        contentView.add(createButtonBar(multiView))
        contentView.add(multiView)
        setContentView(contentView)
        createDispatcher(edit)
    }

    private fun createMultiView(edit: EditorSource): MultiView {
        val multiView = MultiView(this, SOLID_KEY)
        multiView.add(createCockpit())
        multiView.add(MapFactory.DEF(this, SOLID_KEY).tracker(edit).toView())
        multiView.add(GraphViewFactory.all(appContext, this, dispatcher, theme, InfoID.TRACKER))
        return multiView
    }

    private fun createCockpit(): View {
        val p = PercentageLayout(this)
        p.setOrientation(AppLayout.getOrientationAlongLargeSide(this))
        val c1 = CockpitView(this, theme)

        c1.add(
            dispatcher, CurrentSpeedDescription(appContext.storage),
            InfoID.SPEED_SENSOR, InfoID.LOCATION
        )
        c1.addAltitude(dispatcher)
        c1.add(dispatcher, PredictiveTimeDescription(), InfoID.TRACKER_TIMER)
        c1.addC(dispatcher, DistanceDescription(appContext.storage), InfoID.TRACKER)
        c1.addC(dispatcher, AverageSpeedDescriptionAP(appContext.storage), InfoID.TRACKER)
        val c2 = CockpitView(this, theme)
        c2.add(dispatcher, MaximumSpeedDescription(appContext.storage), InfoID.TRACKER)
        c2.addHeartRate(dispatcher) // With click to update sensors
        c2.addPower(dispatcher) // With click to update sensors
        c2.addCadence(dispatcher) // With click to update sensors
        p.add(c1, 80)
        p.add(c2, 20)
        return p
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
        // TODO dispatcher.addSource(OverlaysSource(appContext))
        dispatcher.addSource(SensorSource(serviceContext, appContext.broadcaster, InfoID.HEART_RATE_SENSOR))
        dispatcher.addSource(SensorSource(serviceContext, appContext.broadcaster, InfoID.POWER_SENSOR))
        dispatcher.addSource(SensorSource(serviceContext, appContext.broadcaster, InfoID.CADENCE_SENSOR))
        dispatcher.addSource(SensorSource(serviceContext, appContext.broadcaster, InfoID.SPEED_SENSOR))
    }
}
