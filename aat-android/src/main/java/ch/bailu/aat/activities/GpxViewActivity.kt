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
import ch.bailu.aat.views.BusyViewControlIID
import ch.bailu.aat.views.ContentView
import ch.bailu.aat.views.ImageButtonViewGroup
import ch.bailu.aat.views.PercentageLayout
import ch.bailu.aat.views.bar.MainControlBar
import ch.bailu.aat.views.description.mview.MultiView
import ch.bailu.aat.views.graph.GraphViewFactory
import ch.bailu.aat.views.preferences.VerticalScrollView
import ch.bailu.aat_lib.dispatcher.CurrentLocationSource
import ch.bailu.aat_lib.dispatcher.FileViewSource
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface
import ch.bailu.aat_lib.dispatcher.OverlaysSource
import ch.bailu.aat_lib.dispatcher.TrackerSource
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.map.MapViewInterface
import ch.bailu.foc.Foc
import ch.bailu.foc_android.FocAndroid

class GpxViewActivity : ActivityContext(), View.OnClickListener, OnContentUpdatedInterface {
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
                val contentView = ContentView(this, theme)
                val bar = MainControlBar(this)
                contentView.add(bar)
                contentView.add(errorView)
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
        map = MapFactory.DEF(this, SOLID_KEY).externalContent()
        val summary = VerticalScrollView(this)
        summary.addAllContent(this, FileContentActivity.getSummaryData(this), theme, InfoID.FILEVIEW)
        val graph: View = GraphViewFactory.all(appContext, this, this, theme, InfoID.FILEVIEW)
        return if (AppLayout.isTablet(this)) {
            createPercentageLayout(summary, graph)
        } else {
            createMultiView(bar, summary, graph, contentView)
        }
    }

    private fun createMultiView(bar: MainControlBar, summary: View, graph: View, contentView: ContentView): View {
        val mv = MultiView(this, SOLID_KEY)
        mv.add(summary)
        mv.add(To.view(map))
        mv.add(graph)
        bar.addMvNext(mv)
        contentView.addMvIndicator(mv)
        return mv
    }

    private fun createPercentageLayout(summary: View, graph: View): View {
        val a = PercentageLayout(this)
        a.setOrientation(AppLayout.getOrientationAlongLargeSide(this))
        a.add(To.view(map), 60)
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
        bar.setOrientation(LinearLayout.HORIZONTAL)
        bar.addOnClickListener(this)
    }

    private fun createDispatcher() {
        addSource(TrackerSource(serviceContext, appContext.broadcaster))
        addSource(CurrentLocationSource(serviceContext, appContext.broadcaster))
        addSource(OverlaysSource(appContext))
        addSource(FileViewSource(appContext, file))
        addTarget(this, InfoID.FILEVIEW)

        busyControl?.apply {
            addTarget(
                this, InfoID.FILEVIEW,
                InfoID.OVERLAY,
                InfoID.OVERLAY + 1,
                InfoID.OVERLAY + 2,
                InfoID.OVERLAY + 3
            )
        }
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        map?.frameBounding(info.boundingBox)
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
