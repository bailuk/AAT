package ch.bailu.aat_lib.description

import ch.bailu.aat_lib.gpx.attributes.TimeWindowAttributes
import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.resources.Res

/** Displays 10-minute rolling average speed from [TimeWindowAttributes]. */
class WindowSpeedDescription(storage: StorageInterface) : SpeedDescription(storage, FormatDisplay.f().decimal2) {
    override fun getLabel(): String {
        return "10' " + Res.str().average()
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        setCache(info.getAttributes().getAsFloat(TimeWindowAttributes.INDEX_WINDOW_SPEED))
    }
}
