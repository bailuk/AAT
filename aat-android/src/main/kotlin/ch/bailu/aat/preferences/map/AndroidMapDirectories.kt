package ch.bailu.aat.preferences.map

import android.content.Context
import ch.bailu.aat.preferences.Storage
import ch.bailu.aat.util.fs.AndroidVolumes
import ch.bailu.aat_lib.preferences.SelectionList
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.map.MapDirectories
import ch.bailu.aat_lib.preferences.map.SolidMapsForgeDirectoryHint
import ch.bailu.aat_lib.preferences.map.SolidMapsForgeMapFile
import ch.bailu.aat_lib.preferences.map.SolidRenderTheme
import ch.bailu.aat_lib.util.fs.AppDirectory
import ch.bailu.foc.Foc
import ch.bailu.foc.FocFactory
import ch.bailu.foc_android.FocAndroid
import ch.bailu.foc_android.FocAndroidFactory
import java.io.File

class AndroidMapDirectories(private val context: Context) : MapDirectories {
    override fun getWellKnownMapDirs(): ArrayList<Foc> {
        val result = ArrayList<Foc>()
        val volumes = AndroidVolumes(context)
        for (f in volumes.volumes) {
            SelectionList.addDr(result, f.child(MAPS_DIR))
            SelectionList.addDr(
                result,
                f.child(AppDirectory.DIR_AAT_DATA + "/" + MAPS_DIR)
            )
            SelectionList.addDr(result, f.child(ORUX_MAPS_DIR))
        }

        // app_private/files/maps (readable and on external medium)
        val files = volumes.files
        for (i in 1 until files.size) {
            SelectionList.addDr(result, files[i].child(MAPS_DIR))
        }
        return result
    }

    override fun getDefault(): Foc? {
        val external = context.getExternalFilesDir(null)
        return if (external is File) {
            FocAndroid.factory(context, external.absolutePath)
        } else {
            null
        }
    }

    override fun createSolidDirectory(): SolidMapsForgeDirectoryHint {
        val foc: FocFactory = FocAndroidFactory(context)
        val storage: StorageInterface = Storage(context)
        return SolidMapsForgeDirectoryHint(storage, foc, this)
    }

    override fun createSolidFile(): SolidMapsForgeMapFile {
        val factory: FocFactory = FocAndroidFactory(context)
        return SolidMapsForgeMapFile(createSolidDirectory(), factory)
    }

    override fun createSolidRenderTheme(): SolidRenderTheme {
        return SolidRenderTheme(createSolidDirectory(), FocAndroidFactory(context))
    }

    companion object {
        const val MAPS_DIR = "maps"
        const val ORUX_MAPS_DIR = "oruxmaps/mapfiles"
    }
}
