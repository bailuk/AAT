package ch.bailu.aat.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import ch.bailu.aat.R
import ch.bailu.aat.app.AndroidInformationUtil
import ch.bailu.aat.broadcaster.addMapOverlaySources
import ch.bailu.aat.map.MapFactory
import ch.bailu.aat.map.To
import ch.bailu.aat.util.ui.AppLayout
import ch.bailu.aat.util.ui.theme.AppTheme
import ch.bailu.aat.views.bar.ControlBar
import ch.bailu.aat.views.bar.MainControlBar
import ch.bailu.aat.views.layout.ContentView
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.coordinates.WGS84Coordinates
import ch.bailu.aat_lib.dispatcher.Dispatcher
import ch.bailu.aat_lib.dispatcher.SourceInterface
import ch.bailu.aat_lib.dispatcher.source.CurrentLocationSource
import ch.bailu.aat_lib.dispatcher.source.EditorSource
import ch.bailu.aat_lib.dispatcher.source.TrackerSource
import ch.bailu.aat_lib.dispatcher.usage.UsageTrackerAlwaysEnabled
import ch.bailu.aat_lib.dispatcher.usage.UsageTrackerInterface
import ch.bailu.aat_lib.dispatcher.usage.UsageTrackers
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.map.MapViewInterface
import ch.bailu.aat_lib.util.Objects

class MapActivity : AbsKeepScreenOnActivity() {
    companion object {
        const val SOLID_KEY = "map"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val usageTrackers = UsageTrackers()
        val overlayUsageTracker = usageTrackers.createOverlayUsageTracker(appContext.storage,
            *AndroidInformationUtil.mapOverlayInfoIdList.toIntArray())

        val edit = EditorSource(appContext, UsageTrackerAlwaysEnabled())
        val contentView = ContentView(this, AppTheme.cockpit)
        val map = createMap(edit, overlayUsageTracker)
        contentView.add(To.view(map)!!)
        setContentView(contentView)
        createDispatcher(dispatcher, appContext, edit, usageTrackers)
        contentView.showTip(getString(R.string.tt_map_edges))
        handleIntent(map)
    }

    private fun handleIntent(map: MapViewInterface) {
        val intent = intent
        val uri = intent.data
        if (Objects.equals(intent.action, Intent.ACTION_VIEW) && uri != null) {
            setMapCenterFromUri(map, uri)
        }
    }

    private fun setMapCenterFromUri(map: MapViewInterface, uri: Uri) {
        try {
            val c = WGS84Coordinates(uri.toString()).toLatLong()
            map.setCenter(c)
        } catch (e: Exception) {
            AppLog.w(this, e)
        }
    }

    private fun createMap(edit: EditorSource, usageTracker: UsageTrackerInterface): MapViewInterface {
        return MapFactory.createDefaultMapView(this, SOLID_KEY).map(edit, createButtonBar(), usageTracker)
    }

    private fun createDispatcher(dispatcher: Dispatcher, appContext: AppContext, edit: SourceInterface, usageTrackers: UsageTrackers) {
        val serviceContext = appContext.services

        dispatcher.addSource(edit)
        dispatcher.addSource(TrackerSource(serviceContext, appContext.broadcaster, usageTrackers))
        dispatcher.addSource(CurrentLocationSource(serviceContext, appContext.broadcaster))

        dispatcher.addMapOverlaySources(appContext, usageTrackers)
    }

    private fun createButtonBar(): ControlBar {
        val bar = MainControlBar(this)
        bar.addActivityCycle(this)
        if (AppLayout.haveExtraSpaceGps(this)) {
            bar.addSpace()
        }
        bar.addGpsState(this)
        bar.addTrackerState(this)
        return bar
    }
}
