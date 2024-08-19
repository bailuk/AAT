package ch.bailu.aat_lib.description

import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.resources.Res

class PathDescription : NameDescription() {
    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        updateName(info.getFile().pathName)
    }

    override fun getLabel(): String {
        return Res.str().d_path()
    }
}
