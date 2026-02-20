package ch.bailu.aat_lib.preferences.map.overlay

import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.preferences.system.SolidDataDirectory
import ch.bailu.aat_lib.util.fs.AppDirectory

class SolidDraftOverlay(baseDirectory: SolidDataDirectory) :
    SolidFixedOverlay(baseDirectory,
        InfoID.EDITOR_DRAFT,
        AppDirectory.DIR_EDIT,
        AppDirectory.FILE_DRAFT)
