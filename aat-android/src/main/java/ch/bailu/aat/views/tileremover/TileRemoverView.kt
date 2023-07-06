package ch.bailu.aat.views.tileremover

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.LinearLayout
import ch.bailu.aat.R
import ch.bailu.aat.dispatcher.AndroidBroadcaster.Companion.register
import ch.bailu.aat.menus.RemoveTilesMenu
import ch.bailu.aat.preferences.map.AndroidSolidTileCacheDirectory
import ch.bailu.aat.services.ServiceContext
import ch.bailu.aat.util.ui.theme.AppTheme
import ch.bailu.aat.util.ui.theme.UiTheme
import ch.bailu.aat.views.busy.BusyViewControl
import ch.bailu.aat.views.ImageButtonViewGroup
import ch.bailu.aat.views.bar.ControlBar
import ch.bailu.aat_lib.dispatcher.AppBroadcaster
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.map.SolidTileCacheDirectory

class TileRemoverView(sc: ServiceContext, activity: Activity, theme: UiTheme) :
    LinearLayout(sc.getContext()), View.OnClickListener, OnPreferencesChanged {
    private val summaryView: TileSummariesView = TileSummariesView(activity, theme)
    private val scan: ImageButtonViewGroup = ImageButtonViewGroup(context, R.drawable.view_refresh_inverse)
    private val remove: ImageButtonViewGroup = ImageButtonViewGroup(context, R.drawable.user_trash_inverse)
    private val busyScan: BusyViewControl =
        BusyViewControl(scan)
    private val busyRemove: BusyViewControl =
        BusyViewControl(remove)
    private val sdirectory: SolidTileCacheDirectory
    private val scontext: ServiceContext
    private val acontext: Activity

    private fun addW(view: View) {
        addView(view)
        val l = view.layoutParams as LayoutParams
        l.weight = 1f
        view.layoutParams = l
    }

      private fun createControlBar(context: Context, theme: UiTheme): View {
        val bar = ControlBar(context, VERTICAL, AppTheme.bar)
        bar.addButton(scan)
        bar.addButton(remove)
        bar.addOnClickListener(this)
        theme.button(scan)
        theme.button(remove)
        theme.background(bar)
        return bar
    }

    public override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        sdirectory.register(this)
        register(
            context,
            onTileRemoverStopped,
            AppBroadcaster.TILE_REMOVER_STOPPED
        )
        register(
            context,
            onTileRemoverScan,
            AppBroadcaster.TILE_REMOVER_SCAN
        )
        register(
            context,
            onTileRemoverRemove,
            AppBroadcaster.TILE_REMOVER_REMOVE
        )
    }

    private val onTileRemoverStopped: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            busyScan.stopWaiting()
            busyRemove.stopWaiting()
            updateText()
        }
    }
    private val onTileRemoverRemove: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            busyRemove.startWaiting()
            busyScan.stopWaiting()
            updateText()
        }
    }
    private val onTileRemoverScan: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            busyScan.startWaiting()
            busyRemove.stopWaiting()
            updateText()
        }
    }

    init {
        orientation = HORIZONTAL
        scontext = sc
        acontext = activity
        sdirectory = AndroidSolidTileCacheDirectory(context)
        addW(summaryView)
        addView(createControlBar(context, theme))
    }

    fun updateText() {
        scontext.insideContext {
            val tr = scontext.getTileRemoverService()
            summaryView.updateInfo(tr.summaries)
        }
    }

    override fun onClick(v: View) {
        scontext.insideContext {
            val tr = scontext.getTileRemoverService()
            if (v === scan && busyScan.isWaiting()) {
                tr.state.stop()
            } else if (v === scan) {
                tr.state.scan()
            } else if (v === remove && busyRemove.isWaiting()) {
                tr.state.stop()
            } else if (v === remove) { // show menu
                RemoveTilesMenu(scontext, acontext).showAsDialog(scontext.getContext())
            }
        }
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {
        scontext.insideContext {
            val tr = scontext.getTileRemoverService()
            if (sdirectory.hasKey(key)) {
                tr.state.reset()
            } else if (key.contains("SolidTrim")) {
                tr.state.rescan()
            }
        }
    }

    public override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        sdirectory.unregister(this)
        context.unregisterReceiver(onTileRemoverStopped)
        context.unregisterReceiver(onTileRemoverScan)
        context.unregisterReceiver(onTileRemoverRemove)
    }
}
