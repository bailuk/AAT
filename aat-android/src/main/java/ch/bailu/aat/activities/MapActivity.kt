package ch.bailu.aat.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import ch.bailu.aat.R
import ch.bailu.aat.map.MapFactory
import ch.bailu.aat.map.To
import ch.bailu.aat.util.ui.AppLayout
import ch.bailu.aat.util.ui.AppTheme
import ch.bailu.aat.views.ContentView
import ch.bailu.aat.views.bar.ControlBar
import ch.bailu.aat.views.bar.MainControlBar
import ch.bailu.aat_lib.coordinates.WGS84Coordinates
import ch.bailu.aat_lib.description.EditorSource
import ch.bailu.aat_lib.dispatcher.CurrentLocationSource
import ch.bailu.aat_lib.dispatcher.OverlaysSource
import ch.bailu.aat_lib.dispatcher.TrackerSource
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.map.MapViewInterface
import ch.bailu.aat_lib.util.Objects

class MapActivity : AbsKeepScreenOnActivity() {
    companion object {
        const val SOLID_KEY = "map"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val edit = EditorSource(appContext)
        val contentView = ContentView(this, AppTheme.cockpit)
        val map = createMap(edit)
        contentView.add(errorView)
        contentView.add(To.view(map))
        setContentView(contentView)
        createDispatcher(edit)
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

    private fun createMap(edit: EditorSource): MapViewInterface {
        return MapFactory.DEF(this, SOLID_KEY).map(edit, createButtonBar())
    }

    private fun createDispatcher(edit: EditorSource) {
        addSource(edit)
        addSource(TrackerSource(serviceContext, appContext.broadcaster))
        addSource(CurrentLocationSource(serviceContext, appContext.broadcaster))
        addSource(OverlaysSource(appContext))
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
