package ch.bailu.aat_lib.description

import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.resources.Res

/** Displays the full file path of the track. */
class PathDescription : NameDescription() {
    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        updateName(info.getFile().pathName)
    }

    override fun getLabel(): String {
        return Res.str().d_path()
    }
}
