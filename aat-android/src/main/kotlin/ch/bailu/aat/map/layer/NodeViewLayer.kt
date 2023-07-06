package ch.bailu.aat.map.layer

import android.content.Context
import android.view.View
import ch.bailu.aat.activities.NodeDetailActivity
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.util.Point
import ch.bailu.foc.Foc

open class NodeViewLayer(appContext: AppContext, private val context: Context, mc: MapContext) :
    AbsNodeViewLayer(appContext, context, mc) {

    private var file: Foc? = null
    private var index = 0

    override fun onLongClick(view: View): Boolean {
        return false
    }

    override fun onClick(v: View) {
        val file = file
        if (file is Foc && file.isFile) {
            startNodeDetailActivity(file.path)
        }
    }

    protected fun startNodeDetailActivity(path: String) {
        NodeDetailActivity.start(context, path, index)
    }

    override fun setSelectedNode(iid: Int, info: GpxInformation, node: GpxPointNode, index: Int) {
        super.setSelectedNode(iid, info, node, index)
        this.index = index
        file = info.file
        markupBuilder.appendInfo(info, this.index)
        markupBuilder.appendNode(node, info)
        markupBuilder.appendAttributes(node.attributes)
        setHtmlText(markupBuilder)
    }

    override fun onAttached() {}
    override fun onDetached() {}
    override fun onTap(tapPos: Point): Boolean { return false }
}
