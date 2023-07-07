package ch.bailu.aat_lib.description

import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.resources.Res

class TrackSizeDescription : ContentDescription() {
    private var value = ""
    private var size = -1
    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        val track = info.gpxList
        if (track != null && size != track.pointList.size()) {
            size = track.pointList.size()
            value = "P: " + track.pointList.size() +
                    ", M: " + track.markerList.size() +
                    ", S: " + track.segmentList.size()
        }
    }

    override fun getValue(): String {
        return value
    }

    override fun getLabel(): String {
        return Res.str().d_size()
    }
}
