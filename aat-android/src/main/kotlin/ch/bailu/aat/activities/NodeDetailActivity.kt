package ch.bailu.aat.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import ch.bailu.aat.R
import ch.bailu.aat.app.ActivitySwitcher
import ch.bailu.aat.map.MapFactory
import ch.bailu.aat.map.To
import ch.bailu.aat.util.ui.AppLayout
import ch.bailu.aat.util.ui.theme.AppTheme
import ch.bailu.aat.views.layout.ContentView
import ch.bailu.aat.views.image.ImageButtonViewGroup
import ch.bailu.aat.views.layout.PercentageLayout
import ch.bailu.aat.views.image.SVGAssetView
import ch.bailu.aat.views.bar.ControlBar
import ch.bailu.aat.views.bar.MainControlBar
import ch.bailu.aat.views.graph.GraphView
import ch.bailu.aat.views.graph.GraphViewFactory
import ch.bailu.aat.views.html.HtmlScrollTextView
import ch.bailu.aat_lib.dispatcher.source.CurrentLocationSource
import ch.bailu.aat_lib.dispatcher.TargetInterface
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.gpx.GpxListArray
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.html.MarkupBuilderGpx
import ch.bailu.aat_lib.map.MapViewInterface
import ch.bailu.foc.Foc

class NodeDetailActivity : ActivityContext(), View.OnClickListener, TargetInterface, OnSeekBarChangeListener {
    private var nextNode: ImageButtonViewGroup? = null
    private var previousNode: ImageButtonViewGroup? = null
    private var icon: SVGAssetView? = null
    private var mapView: MapViewInterface? = null
    private var htmlView: HtmlScrollTextView? = null
    private var graph: GraphView? = null
    private var seekBar: SeekBar? = null
    private var file = Foc.FOC_NULL
    private var arrayCache = GpxListArray(GpxList.NULL_ROUTE)
    private var infoCache = GpxInformation.NULL
    private var markupBuilder: MarkupBuilderGpx? = null
    private val theme = AppTheme.trackContent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        markupBuilder = MarkupBuilderGpx(appContext.storage)
        file = appContext.toFoc(intent.getStringExtra("ID"))
        val contentView = ContentView(this, theme)
        contentView.add(createButtonBar())
        contentView.add(createSeekBar())
        contentView.add(createVerticalView())
        createDispatcher()
        setContentView(contentView)
    }

    private fun createButtonBar(): ControlBar {
        val bar: ControlBar = MainControlBar(this)
        previousNode = bar.addImageButton(R.drawable.go_up_inverse)
        nextNode = bar.addImageButton(R.drawable.go_down_inverse)
        val icon = SVGAssetView(serviceContext, 0)
        this.icon = icon
        bar.add(icon)
        bar.orientation = LinearLayout.HORIZONTAL
        bar.addOnClickListener(this)
        return bar
    }

    private fun createVerticalView(): View {
        val viewA = PercentageLayout(this)
        val viewB = PercentageLayout(this)
        viewB.setOrientation(AppLayout.getOrientationAlongLargeSide(this))

        htmlView = HtmlScrollTextView(this).apply {
            enableAutoLink()
            themify(theme)
            viewB.add(this, 40)
        }

        mapView = MapFactory.DEF(this, SOLID_KEY).node().apply {
            viewB.add(To.view(this)!!, 60)
        }

        graph = GraphViewFactory.createAltitudeGraph(appContext, this, theme)
        viewA.add(graph!!, 20)
        viewA.add(viewB, 80)
        return viewA
    }

    private fun createSeekBar(): SeekBar {
        val seekBar = SeekBar(this)
        seekBar.setOnSeekBarChangeListener(this)
        this.seekBar = seekBar
        return seekBar
    }

    private fun createDispatcher() {
        dispatcher.addTarget(this, InfoID.FILE_VIEW)
        dispatcher.addSource(CurrentLocationSource(serviceContext, appContext.broadcaster))
        // TODO dispatcher.addSource(FileViewSource(appContext, file))
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        arrayCache = GpxListArray(info.getGpxList())
        infoCache = info
        graph?.setVisibility(info)
        setSeekBarMax()
        val index = intent.getIntExtra("I", 0)
        updateToIndex(index)
    }

    private fun setSeekBarMax() {
        seekBar?.max = Math.max(0, arrayCache.size() - 1)
    }

    private fun updateToIndex(newIndex: Int) {
        if (arrayCache.size() > 0) {
            val index = if (newIndex < 0) {
                arrayCache.size() - 1
            } else if (newIndex >= arrayCache.size()) {
                0
            } else {
                newIndex
            }

            mapView?.setCenter(arrayCache[index].getBoundingBox().center.toLatLong())
            markupBuilder?.appendInfo(infoCache, index)
            markupBuilder?.appendNode(arrayCache[index], infoCache)
            markupBuilder?.appendAttributes(arrayCache[index].getAttributes())
            htmlView?.setHtmlText(markupBuilder.toString())
            markupBuilder?.clear()
            graph?.onContentUpdated(infoCache, index)
            seekBar?.progress = index
            icon?.setImageObject(arrayCache[index])
        }
    }

    override fun onClick(view: View) {
        if (arrayCache.size() > 0) {
            val index = if (view === previousNode) {
                arrayCache.index - 1
            } else if (view === nextNode) {
                arrayCache.index + 1
            } else {
                arrayCache.index
            }
            updateToIndex(index)
        }
    }

    override fun onProgressChanged(seekBar: SeekBar, i: Int, fromUser: Boolean) {
        if (fromUser) updateToIndex(i)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {}
    override fun onStopTrackingTouch(seekBar: SeekBar) {}

    companion object {
        private val SOLID_KEY = NodeDetailActivity::class.java.simpleName
        @JvmStatic
        fun start(context: Context, fileId: String, index: Int) {
            val intent = Intent()
            intent.putExtra("I", index)
            intent.putExtra("ID", fileId)
            ActivitySwitcher.start(context, NodeDetailActivity::class.java, intent)
        }
    }
}
