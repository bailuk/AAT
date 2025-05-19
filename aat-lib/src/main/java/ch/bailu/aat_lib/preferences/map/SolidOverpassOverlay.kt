package ch.bailu.aat_lib.preferences.map

import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.preferences.system.SolidDataDirectory
import ch.bailu.aat_lib.util.fs.AppDirectory

class SolidOverpassOverlay (baseDirectory: SolidDataDirectory):
    SolidFixedOverlay(baseDirectory, InfoID.OVERPASS, AppDirectory.DIR_OVERPASS, AppDirectory.FILE_OVERPASS)
