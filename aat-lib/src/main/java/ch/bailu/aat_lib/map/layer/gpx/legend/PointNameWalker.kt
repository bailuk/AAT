package ch.bailu.aat_lib.map.layer.gpx.legend

import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.gpx.GpxSegmentNode
import ch.bailu.aat_lib.gpx.attributes.Keys.Companion.toIndex

class PointNameWalker : LegendWalker() {
    private var displayed = 0
    override fun doList(track: GpxList): Boolean {
        displayed = 0
        return super.doList(track)
    }

    override fun doMarker(marker: GpxSegmentNode): Boolean {
        return displayed < LIMIT && legendContext!!.isVisible(marker.getBoundingBox())
    }

    override fun doPoint(point: GpxPointNode) {
        if (displayed < LIMIT) {
            legendContext!!.setB(point)
            if (!legendContext!!.arePointsTooClose()) {
                drawLegendFromB()
                legendContext!!.switchNodes()
            }
        }
    }

    private fun drawLegendFromB() {
        if (legendContext!!.isBVisible) {
            val name = nameFromB
            if (name != null) {
                legendContext!!.drawLabelB(name)
                displayed++
            }
        }
    }

    private val nameFromB: String?
        get() {
            val attr = legendContext!!.nodes.nodeB.point.getAttributes()
            return if (attr.hasKey(KEY_INDEX_NAME)) attr[KEY_INDEX_NAME] else null
        }

    companion object {
        private const val LIMIT = 20
        private val KEY_INDEX_NAME = toIndex("name")
    }
}
