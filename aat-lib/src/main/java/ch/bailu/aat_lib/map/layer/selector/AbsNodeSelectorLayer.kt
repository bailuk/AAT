package ch.bailu.aat_lib.map.layer.selector

import ch.bailu.aat_lib.coordinates.BoundingBoxE6
import ch.bailu.aat_lib.dispatcher.TargetInterface
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
import org.mapsforge.core.model.LatLong

abstract class AbsNodeSelectorLayer(
    private val services: ServicesInterface,
    s: StorageInterface,
    mc: MapContext,
    private val pos: Position
) : MapLayerInterface, TargetInterface, EdgeViewInterface {

    private val squareSize = mc.getMetrics().getDensity().toPixelInt(SQUARE_SIZE.toFloat())
    private val squareHSize = mc.getMetrics().getDensity().toPixelInt(SQUARE_HSIZE.toFloat())

    private var visible = true

    private val infoCache = IndexedMap<Int, GpxInformation>()
    private val centerRect = Rect()

    private var selectedNode: GpxPointNode? = null


    private val sgrid = SolidMapGrid(s, mc.getSolidKey())
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
                mcontext.getMetrics().getWidth() / 2 - squareHSize,
                mcontext.getMetrics().getHeight() / 2 - squareHSize
            )

            val lt = mcontext.getMetrics().fromPixel(centerRect.left, centerRect.top)
            val rb = mcontext.getMetrics().fromPixel(centerRect.right, centerRect.bottom)

            if (lt is LatLong && rb is LatLong) {
                val centerBounding = BoundingBoxE6()
                centerBounding.add(lt)
                centerBounding.add(rb)
                findNodeAndNotify(centerBounding)
            }

            centerRect.offset(mcontext.getMetrics().getLeft(), mcontext.getMetrics().getTop())
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
        val node = selectedNode
        if (node == null || !centerBounding.contains(node)) {
            findNode(centerBounding)
        }
    }

    private fun findNode(centerBounding: BoundingBoxE6) {
        for (index in 0 until infoCache.size()) {
            val info = infoCache.getValueAt(index)
            val infoID = infoCache.getKeyAt(index)

            if (info is GpxInformation && infoID is Int) {
                val finder = GpxNodeFinder(centerBounding)
                finder.walkTrack(info.getGpxList())

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
            val selectedPixel = mcontext.getMetrics().toPixel(node)
            mcontext.draw()
                .bitmap(mcontext.draw().getNodeBitmap(), selectedPixel, MapColor.NODE_SELECTED)
        }
    }

    private fun drawCenterSquare(mcontext: MapContext) {
        mcontext.draw().rect(centerRect, mcontext.draw().getGridPaint())
        mcontext.draw().point(mcontext.getMetrics().getCenterPixel())
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        if (info.getLoaded()) {
            infoCache.put(iid, info)
        } else {
            infoCache.remove(iid)
        }
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {
        if (sgrid.hasKey(key)) {
            coordinates = sgrid.createCenterCoordinatesLayer(services)
        }
    }

    override fun show() { visible = true }
    override fun hide() { visible = false }
    override fun pos() : Position { return pos }
}
