package ch.bailu.aat_lib.preferences.map

import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.preferences.system.SolidDataDirectory
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.util.fs.AppDirectory

class SolidOverpassOverlay (baseDirectory: SolidDataDirectory):
    SolidFixedOverlay(baseDirectory, InfoID.OVERPASS, AppDirectory.DIR_OVERPASS, AppDirectory.FILE_OVERPASS)
{

    override fun getLabel(): String {
        return Res.str().query_overpass()
    }
}
