package ch.bailu.aat_lib.gpx

import ch.bailu.foc.Foc

class GpxFileWrapper(private val file: Foc, private val list: GpxList) : GpxInformation() {
    init {
        if (list.pointList.size() > 0) {
            val last = list.pointList.last

            if (last is GpxPointNode) {
                setVisibleTrackPoint(last)
            }
        }
        setVisibleTrackSegment(list.getDelta())
    }

    override fun isLoaded(): Boolean {
        return true
    }

    override fun getGpxList(): GpxList {
        return list
    }

    override fun getFile(): Foc {
        return file
    }
}
