package ch.bailu.aat_lib.preferences.map

import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.preferences.system.SolidDataDirectory
import ch.bailu.aat_lib.util.fs.AppDirectory

class SolidPoiOverlay(baseDirectory: SolidDataDirectory) :
    SolidOverlay(baseDirectory, InfoID.POI, AppDirectory.DIR_POI, "poi.gpx")
