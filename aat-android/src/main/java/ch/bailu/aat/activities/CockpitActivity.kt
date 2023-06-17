package ch.bailu.aat.activities

import android.os.Bundle
import android.view.View
import ch.bailu.aat.dispatcher.SensorSource
import ch.bailu.aat.map.MapFactory
import ch.bailu.aat.util.AndroidTimer
import ch.bailu.aat.util.ui.AppLayout
import ch.bailu.aat.util.ui.theme.AppTheme
import ch.bailu.aat.views.ContentView
import ch.bailu.aat.views.PercentageLayout
import ch.bailu.aat.views.bar.ControlBar
import ch.bailu.aat.views.bar.MainControlBar
import ch.bailu.aat.views.description.CockpitView
import ch.bailu.aat.views.description.mview.MultiView
import ch.bailu.aat.views.graph.GraphViewFactory
import ch.bailu.aat_lib.description.AverageSpeedDescriptionAP
import ch.bailu.aat_lib.description.CurrentSpeedDescription
import ch.bailu.aat_lib.description.DistanceDescription
import ch.bailu.aat_lib.description.EditorSource
import ch.bailu.aat_lib.description.MaximumSpeedDescription
import ch.bailu.aat_lib.description.PredictiveTimeDescription
import ch.bailu.aat_lib.dispatcher.CurrentLocationSource
import ch.bailu.aat_lib.dispatcher.OverlaysSource
import ch.bailu.aat_lib.dispatcher.TrackerSource
import ch.bailu.aat_lib.dispatcher.TrackerTimerSource
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
        contentView.add(errorView)
        contentView.add(multiView)
        setContentView(contentView)
        createDispatcher(edit)
    }

    private fun createMultiView(edit: EditorSource): MultiView {
        val multiView = MultiView(this, SOLID_KEY)
        multiView.add(createCockpit())
        multiView.add(MapFactory.DEF(this, SOLID_KEY).tracker(edit).toView())
        multiView.add(GraphViewFactory.all(appContext, this, this, theme, InfoID.TRACKER))
        return multiView
    }

    private fun createCockpit(): View {
        val p = PercentageLayout(this)
        p.setOrientation(AppLayout.getOrientationAlongLargeSide(this))
        val c1 = CockpitView(this, theme)

        c1.add(
            this, CurrentSpeedDescription(appContext.storage),
            InfoID.SPEED_SENSOR, InfoID.LOCATION
        )
        c1.addAltitude(this)
        c1.add(this, PredictiveTimeDescription(), InfoID.TRACKER_TIMER)
        c1.addC(this, DistanceDescription(appContext.storage), InfoID.TRACKER)
        c1.addC(this, AverageSpeedDescriptionAP(appContext.storage), InfoID.TRACKER)
        val c2 = CockpitView(this, theme)
        c2.add(this, MaximumSpeedDescription(appContext.storage), InfoID.TRACKER)
        c2.addHeartRate(this) // With click to update sensors
        c2.addPower(this) // With click to update sensors
        c2.addCadence(this) // With click to update sensors
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
        addSource(edit)
        addSource(TrackerSource(serviceContext, appContext.broadcaster))
        addSource(TrackerTimerSource(serviceContext, AndroidTimer()))
        addSource(CurrentLocationSource(serviceContext, appContext.broadcaster))
        addSource(OverlaysSource(appContext))
        addSource(SensorSource(serviceContext, appContext.broadcaster, InfoID.HEART_RATE_SENSOR))
        addSource(SensorSource(serviceContext, appContext.broadcaster, InfoID.POWER_SENSOR))
        addSource(SensorSource(serviceContext, appContext.broadcaster, InfoID.CADENCE_SENSOR))
        addSource(SensorSource(serviceContext, appContext.broadcaster, InfoID.SPEED_SENSOR))
    }
}
