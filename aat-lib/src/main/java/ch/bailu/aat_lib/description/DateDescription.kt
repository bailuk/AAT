package ch.bailu.aat_lib.description

import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.resources.Res

open class DateDescription : LongDescription() {
    override fun getLabel(): String {
        return Res.str().d_startdate()
    }

    override fun getValue(): String {
        return FormatDisplay.f().localDateTime.format(cache)
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        setCache(info.getStartTime())
    }
}
