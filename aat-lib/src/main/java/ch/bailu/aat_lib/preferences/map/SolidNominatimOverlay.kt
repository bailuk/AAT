package ch.bailu.aat_lib.preferences.map

import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.preferences.system.SolidDataDirectory
import ch.bailu.aat_lib.util.fs.AppDirectory

class SolidNominatimOverlay(baseDirectory: SolidDataDirectory) :
    SolidFixedOverlay(baseDirectory, InfoID.NOMINATIM, AppDirectory.DIR_NOMINATIM, AppDirectory.FILE_NOMINATIM) {

    override fun getLabel(): String {
        return "Nominatim"
    }
}
