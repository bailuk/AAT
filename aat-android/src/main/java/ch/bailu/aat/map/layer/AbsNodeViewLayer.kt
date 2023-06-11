package ch.bailu.aat.map.layer

import android.content.Context
import android.view.View
import ch.bailu.aat.map.To
import ch.bailu.aat.util.ui.AppLayout
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.html.MarkupBuilder
import ch.bailu.aat_lib.html.MarkupBuilderGpx
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.edge.Position
import ch.bailu.aat_lib.map.layer.gpx.GpxVisibleLimit
import ch.bailu.aat_lib.map.layer.selector.AbsNodeSelectorLayer

abstract class AbsNodeViewLayer(
    appContext: AppContext,
    context: Context,
    private val mcontext: MapContext
) : AbsNodeSelectorLayer(appContext.services, appContext.storage, mcontext, Position.BOTTOM),
    View.OnLongClickListener, View.OnClickListener {
    private val infoView: NodeInfoView = NodeInfoView(appContext, context)
    protected val markupBuilder: MarkupBuilderGpx = MarkupBuilderGpx(appContext.storage)
    private val pos: Placer = Placer(context)

    init {
        infoView.setOnLongClickListener(this)
        infoView.setOnClickListener(this)
        infoView.visibility = View.GONE
        To.view(mcontext.mapView)?.addView(infoView)
    }

    override fun setSelectedNode(iid: Int, info: GpxInformation, node: GpxPointNode, index: Int) {
        infoView.setBackgroundColorFromIID(iid)
        val limit = GpxVisibleLimit(mcontext)
        limit.walkTrack(info.gpxList)
        setGraph(info, index, limit.firstPoint, limit.lastPoint)
    }

    fun setGraph(info: GpxInformation, index: Int, firstPoint: Int, lastPoint: Int) {
        infoView.setGraph(info, index, firstPoint, lastPoint)
        measure()
        layout()
    }

    fun setBackgroundColorFromIID(iid: Int) {
        infoView.setBackgroundColorFromIID(iid)
    }

    fun setHtmlText(html: MarkupBuilder) {
        infoView.setHtmlText(html.toString())
        html.clear()
        measure()
        layout()
    }

    fun showAtLeft() {
        pos.toLeft()
        show()
    }

    fun showAtRight() {
        pos.toRight()
        show()
    }

    override fun hide() {
        AppLayout.fadeOut(infoView)
        mcontext.mapView.requestRedraw()
    }

    override fun show() {
        measure()
        layout()
        AppLayout.fadeIn(infoView)
        mcontext.mapView.requestRedraw()
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        pos.setSize(r - l, b - t)
    }

    private fun layout() {
        infoView.layout(
            pos.x(),
            pos.y(),
            pos.x() + pos.w(),
            pos.y() + pos.h()
        )
    }

    private fun measure() {
        val wspec = View.MeasureSpec.makeMeasureSpec(
            pos.w(),
            View.MeasureSpec.EXACTLY
        )
        val hspec = View.MeasureSpec.makeMeasureSpec(
            pos.h(),
            View.MeasureSpec.EXACTLY
        )
        infoView.measure(wspec, hspec)
    }

    private class Placer(c: Context) {
        private var xoffset = 0
        private var width = 0
        private var height = 0
        private var rightSpace = 0
        private val buttonSpace: Int = AppLayout.getBigButtonSize(c)

        fun setSize(w: Int, h: Int) {
            height = minOf(h / 3, buttonSpace * 3)
            width = minOf(w - buttonSpace, buttonSpace * 5)
            rightSpace = w - width - buttonSpace
        }

        fun toLeft() {
            xoffset = rightSpace
        }

        fun toRight() {
            xoffset = buttonSpace
        }

        fun x(): Int {
            return xoffset
        }

        fun y(): Int {
            return 0
        }

        fun h(): Int {
            return height
        }

        fun w(): Int {
            return width
        }
    }
}
