package ch.bailu.aat_lib.preferences.map.overlay

import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.preferences.system.SolidDataDirectory
import ch.bailu.aat_lib.util.fs.AppDirectory

class SolidCriticalMapOverlay(baseDirectory: SolidDataDirectory) :
    SolidFixedOverlay(baseDirectory,
        InfoID.CRITICAL_MAP,
        AppDirectory.DIR_QUERY,
        AppDirectory.FILE_CRITICAL_MAP)