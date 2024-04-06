package ch.bailu.aat.views.list

import android.graphics.Color
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import ch.bailu.aat.activities.ActivityContext
import ch.bailu.aat.map.mapsforge.MapsForgeViewStatic
import ch.bailu.aat.util.AppHtml.fromHtml
import ch.bailu.aat.util.ui.AppLayout.getBigButtonSize
import ch.bailu.aat.util.ui.theme.AppTheme
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.html.MarkupBuilderGpx
import ch.bailu.aat_lib.map.layer.gpx.GpxDynLayer

class NodeEntryView(activityContext: ActivityContext) : LinearLayout(activityContext) {
    private val map: MapsForgeViewStatic
    private val text: TextView
    private val gpxOverlay: GpxDynLayer
    private val markupBuilder: MarkupBuilderGpx

    init {
        orientation = HORIZONTAL
        val previewSize = getBigButtonSize(activityContext)
        markupBuilder = MarkupBuilderGpx(activityContext.appContext.storage)
        map = MapsForgeViewStatic(activityContext, activityContext.appContext)
        activityContext.addLifeCycle(map)
        gpxOverlay = GpxDynLayer(
            activityContext.appContext.storage,
            map.getMContext(),
            activityContext.serviceContext
        )
        map.add(gpxOverlay)
        text = TextView(activityContext)
        text.setTextColor(Color.WHITE)
        addViewWeight(text)
        addView(map, previewSize, previewSize)
        THEME.content(text)
        THEME.button(this)
    }

    private fun addViewWeight(v: View) {
        addView(v)
        val l = v.layoutParams as LayoutParams
        l.weight = 1f
        v.layoutParams = l
    }

    fun update(iid: Int, info: GpxInformation, node: GpxPointNode) {
        markupBuilder.appendNode(node, info)
        markupBuilder.appendAttributes(node.getAttributes())
        text.text = fromHtml(markupBuilder.toString())
        markupBuilder.clear()
        val bounding = node.getBoundingBox()
        map.frameBounding(bounding)
        gpxOverlay.onContentUpdated(iid, info)
    }

    companion object {
        private val THEME = AppTheme.search
    }
}
