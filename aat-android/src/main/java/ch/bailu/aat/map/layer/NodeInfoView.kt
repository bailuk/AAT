package ch.bailu.aat.map.layer

import android.content.Context
import android.graphics.Color
import android.widget.LinearLayout
import ch.bailu.aat.util.ui.AppTheme
import ch.bailu.aat.views.PercentageLayout
import ch.bailu.aat.views.graph.GraphView
import ch.bailu.aat.views.graph.GraphViewFactory
import ch.bailu.aat.views.html.HtmlScrollTextView
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.map.MapColor

class NodeInfoView(appContext: AppContext, context: Context) : PercentageLayout(context, 0) {
    private val htmlView: HtmlScrollTextView = HtmlScrollTextView(context)
    private val graphView: GraphView = createGraphView(appContext, context)
    private val limitGraphView: GraphView = createGraphView(appContext, context)
    private var backgroundColor = MapColor.LIGHT

    init {
        setOrientation(LinearLayout.VERTICAL)
        htmlView.textView.setTextColor(MapColor.TEXT)
        htmlView.setBackgroundColor(backgroundColor)
        add(htmlView, 50)
        add(graphView, 25)
        add(limitGraphView, 25)
        setBackgroundColor(Color.TRANSPARENT)
    }

    private fun createGraphView(appContext: AppContext, context: Context): GraphView {
        val g = GraphViewFactory.createAltitudeGraph(appContext, context, AppTheme.bar)
        g.visibility = GONE
        g.setBackgroundColor(MapColor.DARK)
        g.showLabel(false)
        return g
    }

    override fun setOnClickListener(l: OnClickListener?) {
        htmlView.isClickable = true
        htmlView.setOnClickListener(l)
        super.setOnClickListener(l)
    }

    override fun setOnLongClickListener(l: OnLongClickListener?) {
        htmlView.textView.setOnLongClickListener(l)
        super.setOnLongClickListener(l)
    }

    fun setBackgroundColorFromIID(IID: Int) {
        val newBackgroundColor = MapColor.getColorFromIID(IID)
        if (backgroundColor != newBackgroundColor) {
            backgroundColor = newBackgroundColor
            htmlView.setBackgroundColor(toBackgroundColorLight(backgroundColor))
            graphView.setBackgroundColor(toBackgroundColorDark(backgroundColor))
            limitGraphView.setBackgroundColor(toBackgroundColorDark(backgroundColor))
        }
    }

    fun setHtmlText(htmlText: String?) {
        htmlView.setHtmlText(htmlText)
    }

    private fun toBackgroundColorLight(color: Int): Int {
        return MapColor.toLightTransparent(color)
    }

    private fun toBackgroundColorDark(color: Int): Int {
        return MapColor.toDarkTransparent(color)
    }

    fun setGraph(info: GpxInformation?, index: Int, firstPoint: Int, lastPoint: Int) {
        graphView.setVisibility(info)
        graphView.onContentUpdated(InfoID.ALL, info, index)
        limitGraphView.setVisibility(info)
        limitGraphView.setLimit(firstPoint, lastPoint)
        limitGraphView.onContentUpdated(InfoID.ALL, info, index)
    }
}
