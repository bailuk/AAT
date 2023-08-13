package ch.bailu.aat.views.graph

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import ch.bailu.aat.R
import ch.bailu.aat.preferences.Storage
import ch.bailu.aat.util.ui.AndroidAppDensity
import ch.bailu.aat.util.ui.theme.UiTheme
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.dispatcher.DispatcherInterface
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.gpx.interfaces.GpxType
import ch.bailu.aat_lib.preferences.general.SolidUnit
import ch.bailu.aat_lib.view.graph.LabelInterface
import ch.bailu.aat_lib.view.graph.Plotter
import ch.bailu.aat_lib.view.graph.PlotterConfig
import javax.annotation.Nonnull

class GraphView(
    context: Context,
    private val appContext: AppContext,
    plotter: Plotter,
    private val theme: UiTheme
) : ViewGroup(context), OnContentUpdatedInterface, PlotterConfig {
    private val density: AndroidAppDensity = AndroidAppDensity(getContext())
    private var gpxCache = GpxList.NULL_TRACK
    private var nodeIndex = -1
    private val yLabel: LabelOverlay
    private val xLabel: LabelOverlay
    private val plotter: Plotter

    init {
        this.plotter = plotter
        setWillNotDraw(false)
        val sunit = SolidUnit(Storage(context))
        xLabel = LabelOverlay(context, Gravity.START or Gravity.BOTTOM)
        yLabel = LabelOverlay(context, Gravity.END or Gravity.TOP)
        xLabel.gravity = Gravity.BOTTOM
        xLabel.setText(Color.WHITE, R.string.distance, sunit.distanceUnit)
        addView(xLabel)
        addView(yLabel)
        setBackgroundColor(theme.getGraphBackgroundColor())
        plotter.initLabels(yLabel)
    }

    override fun onContentUpdated(iid: Int, @Nonnull info: GpxInformation) {
        onContentUpdated(info, -1)
    }

    fun onContentUpdated(info: GpxInformation, index: Int) {
        gpxCache = info.gpxList
        nodeIndex = index
        invalidate()
    }

    public override fun onDraw(canvas: Canvas) {
        if (width > 0 && height > 0) {
            plotter.plot(AndroidCanvas(canvas, appContext, density, theme), this)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        xLabel.layout(0, 0, r - l, b - t)
        yLabel.layout(0, 0, r - l, b - t)
    }

    fun hideXLabel(): GraphView {
        xLabel.visibility = GONE
        return this
    }

    override fun getList(): GpxList {
        return gpxCache
    }

    override fun getIndex(): Int {
        return nodeIndex
    }

    override fun isXLabelVisible(): Boolean {
        return xLabel.visibility == VISIBLE
    }

    override fun getLabels(): LabelInterface {
        return yLabel
    }

    override fun onMeasure(wSpec: Int, hSpec: Int) {
        val width = MeasureSpec.getSize(wSpec)
        val height = MeasureSpec.getSize(hSpec)

        // As big as possible
        val wSpecMeasured = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY)
        val hSpecMeasured = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        xLabel.measure(wSpecMeasured, hSpecMeasured)
        yLabel.setTextSizeFromHeight(height)
        yLabel.measure(wSpecMeasured, hSpecMeasured)
        setMeasuredDimension(width, height)
    }

    fun showLabel(b: Boolean) {
        if (b) {
            xLabel.visibility = VISIBLE
            yLabel.visibility = VISIBLE
        } else {
            xLabel.visibility = GONE
            yLabel.visibility = GONE
        }
    }

    fun setVisibility(info: GpxInformation) {
        visibility =
            if (info.isLoaded && info.getType() == GpxType.ROUTE || info.getType() == GpxType.TRACK) {
                VISIBLE
            } else {
                GONE
            }
    }

    fun connect(di: DispatcherInterface, vararg iid: Int): GraphView {
        di.addTarget(this, *iid)
        return this
    }

    fun setLimit(firstPoint: Int, lastPoint: Int) {
        plotter.setLimit(firstPoint, lastPoint)
    }
}
