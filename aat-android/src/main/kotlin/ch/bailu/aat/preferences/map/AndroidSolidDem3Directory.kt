package ch.bailu.aat.preferences.map

import android.content.Context
import ch.bailu.aat.preferences.Storage
import ch.bailu.aat.util.fs.AndroidVolumes
import ch.bailu.aat_lib.preferences.SelectionList
import ch.bailu.aat_lib.preferences.map.SolidDem3Directory
import ch.bailu.aat_lib.util.fs.AppDirectory
import ch.bailu.foc_android.FocAndroidFactory

class AndroidSolidDem3Directory(private val context: Context) : SolidDem3Directory(
    Storage(context), FocAndroidFactory(context)
) {
    override fun buildSelection(list: ArrayList<String>): ArrayList<String> {
        val volumes = AndroidVolumes(context)

        // exists and is writeable
        for (vol in volumes.volumes) {
            val dem3 = vol.child(DEM3_DIR)
            val aat = vol.child(AppDirectory.DIR_AAT_DATA + "/" + DEM3_DIR)
            SelectionList.addW(list, aat)
            SelectionList.addW(list, dem3)
        }


        // exists not but parent is writeable
        for (vol in volumes.volumes) {
            val aatDem3 = vol.child(AppDirectory.DIR_AAT_DATA + "/" + DEM3_DIR)
            val dem3 = vol.child(DEM3_DIR)
            if (!aatDem3.exists()) SelectionList.addW(list, aatDem3.parent(), aatDem3)
            if (!dem3.exists()) SelectionList.addW(list, dem3.parent(), dem3)
        }

        // exists and is read only
        for (vol in volumes.volumes) {
            val aatDem3 = vol.child(AppDirectory.DIR_AAT_DATA + "/" + DEM3_DIR)
            val dem3 = vol.child(DEM3_DIR)
            SelectionList.addRo(list, aatDem3)
            SelectionList.addRo(list, dem3)
        }

        // official writeable cache directory
        for (cache in volumes.caches) {
            val dem3 = cache.child(DEM3_DIR)
            SelectionList.addW(list, cache, dem3)
        }
        return list
    }
}
