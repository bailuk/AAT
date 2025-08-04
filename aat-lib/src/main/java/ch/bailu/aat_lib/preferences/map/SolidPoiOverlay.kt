package ch.bailu.aat_lib.preferences.map

import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.preferences.system.SolidDataDirectory
import ch.bailu.aat_lib.util.fs.AppDirectory

class SolidPoiOverlay(baseDirectory: SolidDataDirectory) :
    SolidFixedOverlay(baseDirectory, InfoID.POI, AppDirectory.DIR_POI, AppDirectory.FILE_POI)
