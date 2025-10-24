package ch.bailu.aat_lib.preferences.map.overlay

import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.preferences.system.SolidDataDirectory
import ch.bailu.aat_lib.util.fs.AppDirectory

class SolidBrouterOverlay(baseDirectory: SolidDataDirectory) :
    SolidFixedOverlay(baseDirectory,
        InfoID.BROUTER,
        AppDirectory.DIR_QUERY,
        AppDirectory.FILE_BROUTER)