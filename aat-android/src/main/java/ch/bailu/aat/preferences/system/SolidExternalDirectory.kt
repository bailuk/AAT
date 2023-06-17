package ch.bailu.aat.preferences.system

import android.content.Context
import ch.bailu.aat.R
import ch.bailu.aat.preferences.Storage
import ch.bailu.aat.util.fs.AndroidVolumes
import ch.bailu.aat_lib.preferences.SelectionList
import ch.bailu.aat_lib.preferences.SolidFile
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.util.fs.AppDirectory
import ch.bailu.foc_android.FocAndroidFactory

class SolidExternalDirectory(val context: Context) : SolidFile(
    Storage(
        context
    ), "ExternalDirectory", FocAndroidFactory(context)
) {

    override fun getLabel(): String {
        return Res.str().intro_external_list()
    }

    override fun buildSelection(list: ArrayList<String>): ArrayList<String> {
        val volumes = AndroidVolumes(context)
        list.add(context.getString(R.string.none))
        for (dir in KNOWN_DIRS) {
            for (vol in volumes.volumes) {
                SelectionList.add_w(list, vol.child(dir))
            }
        }
        for (dir in KNOWN_DIRS) {
            for (vol in volumes.volumes) {
                SelectionList.add_ro(list, vol.child(dir))
            }
        }
        return list
    }

    companion object {
        private val KNOWN_DIRS = arrayOf(
            AppDirectory.DIR_AAT_DATA + AppDirectory.DIR_IMPORT,
            "MyTracks/gpx",
            "gpx"
        )
    }
}
