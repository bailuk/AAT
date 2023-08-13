package ch.bailu.aat_lib.description

import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.resources.Res

open class NameDescription : ContentDescription() {
    private var name = ""
    override fun getLabel(): String {
        return Res.str().d_name()
    }

    override fun getUnit(): String {
        return ""
    }

    override fun getValue(): String {
        return name
    }

    fun updateName(s: String): Boolean {
        val r = name != s
        name = s
        return r
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        updateName(info.file.name)
    }
}
