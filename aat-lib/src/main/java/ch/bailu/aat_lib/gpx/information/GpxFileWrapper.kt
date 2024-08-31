package ch.bailu.aat_lib.gpx.information

import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.foc.Foc

class GpxFileWrapper(private val file: Foc, private val list: GpxList) : GpxInformation() {
    init {
        if (list.pointList.size() > 0) {
            val lastNode = list.pointList.last
            if (lastNode is GpxPointNode) {
                setVisibleTrackPoint(lastNode)
            }
        }
        this.setVisibleTrackSegment(list.getDelta())
    }

    override fun getLoaded(): Boolean {
        return true
    }

    override fun getGpxList(): GpxList {
        return list
    }

    override fun getFile(): Foc {
        return file
    }
}
