package ch.bailu.aat_lib.map.layer.selector

import ch.bailu.aat_lib.coordinates.BoundingBoxE6
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.GpxNodeFinder
import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.map.MapColor
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.edge.EdgeViewInterface
import ch.bailu.aat_lib.map.edge.Position
import ch.bailu.aat_lib.map.layer.MapLayerInterface
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.map.SolidMapGrid
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.aat_lib.util.IndexedMap
import ch.bailu.aat_lib.util.Rect

abstract class AbsNodeSelectorLayer(
    private val services: ServicesInterface,
    s: StorageInterface,
    mc: MapContext,
    private val pos: Position
) : MapLayerInterface, OnContentUpdatedInterface, EdgeViewInterface {

    private val squareSize = mc.metrics.density.toPixel_i(SQUARE_SIZE.toFloat())
    private val squareHSize = mc.metrics.density.toPixel_i(SQUARE_HSIZE.toFloat())

    private var visible = true

    private val infoCache = IndexedMap<Int, GpxInformation>()
    private val centerRect = Rect()

    private var selectedNode: GpxPointNode? = null


    private val sgrid = SolidMapGrid(s, mc.solidKey)
    private var coordinates = sgrid.createCenterCoordinatesLayer(services)


    companion object {
        const val SQUARE_SIZE = 30
        const val SQUARE_HSIZE = SQUARE_SIZE / 2
    }

    init {
        centerRect.left = 0
        centerRect.right = squareSize
        centerRect.top = 0
        centerRect.bottom = squareSize
    }

    open fun getSelectedNode(): GpxPointNode? {
        return selectedNode
    }

    override fun drawForeground(mcontext: MapContext) {
        if (visible) {
            centerRect.offsetTo(
                mcontext.metrics.width / 2 - squareHSize,
                mcontext.metrics.height / 2 - squareHSize
            )
            val centerBounding = BoundingBoxE6()
            val lt = mcontext.metrics.fromPixel(centerRect.left, centerRect.top)
            val rb = mcontext.metrics.fromPixel(centerRect.right, centerRect.bottom)
            if (lt != null && rb != null) {
                centerBounding.add(lt)
                centerBounding.add(rb)
                findNodeAndNotify(centerBounding)
            }
            centerRect.offset(mcontext.metrics.left, mcontext.metrics.top)
            drawSelectedNode(mcontext)
            drawCenterSquare(mcontext)
            coordinates.drawForeground(mcontext)
        }
    }

    override fun drawInside(mcontext: MapContext) {
        if (visible) {
            coordinates.drawInside(mcontext)
        }
    }

    private fun findNodeAndNotify(centerBounding: BoundingBoxE6) {
        if (selectedNode == null || !centerBounding.contains(selectedNode)) {
            findNode(centerBounding)
        }
    }

    private fun findNode(centerBounding: BoundingBoxE6) {
        for (index in 0 until infoCache.size()) {
            val info = infoCache.getValueAt(index)
            val infoID = infoCache.getKeyAt(index)

            if (info is GpxInformation && infoID is Int) {
                val finder = GpxNodeFinder(centerBounding)
                finder.walkTrack(info.gpxList)

                if (finder.haveNode()) {
                    selectedNode = finder.node
                    setSelectedNode(infoID, info, finder.node, finder.nodeIndex)
                    break
                }
            }
        }
    }

    abstract fun setSelectedNode(iid: Int, info: GpxInformation, node: GpxPointNode, index: Int)

    private fun drawSelectedNode(mcontext: MapContext) {
        val node = selectedNode
        if (node != null) {
            val selectedPixel = mcontext.metrics.toPixel(node)
            mcontext.draw()
                .bitmap(mcontext.draw().nodeBitmap, selectedPixel, MapColor.NODE_SELECTED)
        }
    }

    private fun drawCenterSquare(mcontext: MapContext) {
        mcontext.draw().rect(centerRect, mcontext.draw().gridPaint)
        mcontext.draw().point(mcontext.metrics.centerPixel)
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        if (info.isLoaded) {
            infoCache.put(iid, info)
        } else {
            infoCache.remove(iid)
        }
    }

    override fun onPreferencesChanged(s: StorageInterface, key: String) {
        if (sgrid.hasKey(key)) {
            coordinates = sgrid.createCenterCoordinatesLayer(services)
        }
    }

    override fun show() { visible = true }
    override fun hide() { visible = false }
    override fun pos() : Position { return pos }
}
