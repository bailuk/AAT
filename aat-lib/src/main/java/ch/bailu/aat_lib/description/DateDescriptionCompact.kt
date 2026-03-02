package ch.bailu.aat_lib.description

import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.resources.Res

/** Displays the track start date/time in a compact format (short time). */
class DateDescriptionCompact : LongDescription() {
    override fun getLabel(): String {
        return Res.str().d_startdate()
    }

    override fun getValue(): String {
        return FormatDisplay.f().localDateTimeCompact.format(cache)
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        setCache(info.getStartTime())
    }
}
