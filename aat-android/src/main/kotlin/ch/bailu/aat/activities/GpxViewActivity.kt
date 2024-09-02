package ch.bailu.aat.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import ch.bailu.aat.R
import ch.bailu.aat.map.MapFactory
import ch.bailu.aat.map.To
import ch.bailu.aat.menus.ContentMenu
import ch.bailu.aat.util.fs.AndroidFileAction
import ch.bailu.aat.util.ui.AppLayout
import ch.bailu.aat.util.ui.theme.AppTheme
import ch.bailu.aat.util.ui.tooltip.ToolTip
import ch.bailu.aat.views.busy.BusyViewControlIID
import ch.bailu.aat.views.layout.ContentView
import ch.bailu.aat.views.image.ImageButtonViewGroup
import ch.bailu.aat.views.layout.PercentageLayout
import ch.bailu.aat.views.bar.MainControlBar
import ch.bailu.aat.views.description.mview.MultiView
import ch.bailu.aat.views.graph.GraphViewFactory
import ch.bailu.aat.views.preferences.VerticalScrollView
import ch.bailu.aat_lib.dispatcher.source.CurrentLocationSource
import ch.bailu.aat_lib.dispatcher.TargetInterface
import ch.bailu.aat_lib.dispatcher.source.FileViewSource
import ch.bailu.aat_lib.dispatcher.source.TrackerSource
import ch.bailu.aat_lib.dispatcher.source.addOverlaySources
import ch.bailu.aat_lib.dispatcher.usage.UsageTrackerAlwaysEnabled
import ch.bailu.aat_lib.dispatcher.usage.UsageTrackers
import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.gpx.information.InformationUtil
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.map.MapViewInterface
import ch.bailu.foc.Foc
import ch.bailu.foc_android.FocAndroid

class GpxViewActivity : ActivityContext(), View.OnClickListener, TargetInterface {
    private var fileOperation: ImageButtonViewGroup? = null
    private var copyTo: ImageButtonViewGroup? = null
    private var busyControl: BusyViewControlIID? = null
    private var map: MapViewInterface? = null
    private var file: Foc = Foc.FOC_NULL
    private val theme = AppTheme.trackContent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = intent
        var uri = intent.data
        if (uri == null) {
            if (intent.hasExtra(Intent.EXTRA_STREAM)) {

                @Suppress("DEPRECATION")
                uri = intent.getParcelableExtra(Intent.EXTRA_STREAM)
            }
        }

        if (uri is Uri) {
            try {
                file = FocAndroid.factory(this, uri.toString())
                val contentView =
                    ContentView(this, theme)
                val bar = MainControlBar(this)
                contentView.add(bar)
                val view = createLayout(bar, contentView)
                initButtonBar(bar)
                contentView.add(view)
                busyControl = BusyViewControlIID(contentView)
                setContentView(contentView)
                createDispatcher()
            } catch (e: Exception) {
                AppLog.e(this, e)
            }
        }
    }

    private fun createLayout(bar: MainControlBar, contentView: ContentView): View {
        map = MapFactory.createDefaultMapView(this, SOLID_KEY).externalContent()
        val summary = VerticalScrollView(this)
        summary.addAllContent(dispatcher, FileContentActivity.getSummaryData(this), theme, InfoID.FILE_VIEW)
        val graph: View = GraphViewFactory.all(appContext, this, dispatcher, theme, InfoID.FILE_VIEW)
        return if (AppLayout.isTablet(this)) {
            createPercentageLayout(summary, graph)
        } else {
            createMultiView(bar, summary, graph, contentView)
        }
    }

    private fun createMultiView(bar: MainControlBar, summary: View, graph: View, contentView: ContentView): View {
        val mv = MultiView(this, SOLID_KEY)
        mv.add(summary)
        To.view(map)?.apply { mv.add(this) }
        mv.add(graph)
        bar.addMvNext(mv)
        contentView.addMvIndicator(mv)
        return mv
    }

    private fun createPercentageLayout(summary: View, graph: View): View {
        val a = PercentageLayout(this)
        a.setOrientation(AppLayout.getOrientationAlongLargeSide(this))
        a.add(To.view(map)!!, 60)
        a.add(summary, 40)
        val b = PercentageLayout(this)
        b.add(a, 80)
        b.add(graph, 20)
        return b
    }

    private fun initButtonBar(bar: MainControlBar) {
        val copyTo = bar.addImageButton(R.drawable.document_save_as_inverse)
        this.copyTo = copyTo

        val fileOperation = bar.addImageButton(R.drawable.edit_select_all_inverse)
        this.fileOperation = fileOperation

        ToolTip.set(copyTo, R.string.file_copy)
        ToolTip.set(fileOperation, R.string.tt_menu_file)
        bar.orientation = LinearLayout.HORIZONTAL
        bar.addOnClickListener(this)
    }

    private fun createDispatcher() {
        dispatcher.addSource(TrackerSource(serviceContext, appContext.broadcaster))
        dispatcher.addSource(CurrentLocationSource(serviceContext, appContext.broadcaster))
        dispatcher.addOverlaySources(appContext, UsageTrackers().createOverlayUsageTracker(appContext.storage, *InformationUtil.getOverlayInfoIdList().toIntArray()))
        dispatcher.addSource(FileViewSource(appContext, UsageTrackerAlwaysEnabled()).apply { setFile(file) })
        dispatcher.addTarget(this, InfoID.FILE_VIEW)

        busyControl?.apply {
            dispatcher.addTarget(
                this, InfoID.FILE_VIEW,
                InfoID.OVERLAY,
                InfoID.OVERLAY + 1,
                InfoID.OVERLAY + 2,
                InfoID.OVERLAY + 3
            )
        }
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        map?.frameBounding(info.getBoundingBox())
    }

    override fun onClick(v: View) {
        if (v === copyTo && file.exists()) {
            AndroidFileAction.copyToDir(this, appContext, file)
        } else if (v === fileOperation && file.exists()) {
            fileOperation?.apply {
                ContentMenu(this@GpxViewActivity, file).showAsPopup(this@GpxViewActivity, this)
            }

        }
    }

    companion object {
        private val SOLID_KEY = GpxViewActivity::class.java.simpleName
    }
}
