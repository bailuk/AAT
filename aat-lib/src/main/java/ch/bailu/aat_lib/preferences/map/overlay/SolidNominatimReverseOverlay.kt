package ch.bailu.aat_lib.preferences.map.overlay

import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.preferences.system.SolidDataDirectory
import ch.bailu.aat_lib.util.fs.AppDirectory

class SolidNominatimReverseOverlay(baseDirectory: SolidDataDirectory) :
    SolidFixedOverlay(baseDirectory,
        InfoID.NOMINATIM_REVERSE,
        AppDirectory.DIR_NOMINATIM,
        AppDirectory.FILE_NOMINATIM_REVERSE)