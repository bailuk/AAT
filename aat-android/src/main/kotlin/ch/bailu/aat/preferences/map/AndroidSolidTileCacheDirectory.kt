package ch.bailu.aat.preferences.map

import android.content.Context
import ch.bailu.aat.preferences.Storage
import ch.bailu.aat.util.fs.AndroidVolumes
import ch.bailu.aat_lib.preferences.SelectionList
import ch.bailu.aat_lib.preferences.map.SolidTileCacheDirectory
import ch.bailu.aat_lib.util.fs.AppDirectory
import ch.bailu.foc_android.FocAndroidFactory

class AndroidSolidTileCacheDirectory(private val context: Context) : SolidTileCacheDirectory(
    Storage(context), FocAndroidFactory(context)
) {
    override fun buildSelection(list: ArrayList<String>): ArrayList<String> {
        val volumes = AndroidVolumes(context)
        for (cache in volumes.caches) {
            val tiles = cache.child(AppDirectory.DIR_TILES)
            SelectionList.addW(list, cache, tiles)
        }
        for (vol in volumes.volumes) {
            val osmdroid = vol.child(AppDirectory.DIR_TILES_OSMDROID)
            val aat = vol.child(AppDirectory.DIR_AAT_DATA + "/" + AppDirectory.DIR_TILES)
            SelectionList.addW(list, osmdroid)
            SelectionList.addW(list, aat)
        }
        for (vol in volumes.volumes) {
            val osmdroid = vol.child(AppDirectory.DIR_TILES_OSMDROID)
            val aat = vol.child(AppDirectory.DIR_AAT_DATA + "/" + AppDirectory.DIR_TILES)
            SelectionList.addRo(list, osmdroid)
            SelectionList.addRo(list, aat)
        }
        for (vol in volumes.volumes) {
            val aat = vol.child(AppDirectory.DIR_AAT_DATA + "/" + AppDirectory.DIR_TILES)
            if (!aat.exists()) SelectionList.addRo(list, vol, aat)
        }
        return list
    }
}
