package ch.bailu.aat_gtk.solid

import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.map.MapDirectories
import ch.bailu.aat_lib.preferences.map.SolidMapsForgeDirectory
import ch.bailu.aat_lib.preferences.map.SolidMapsForgeMapFile
import ch.bailu.aat_lib.preferences.map.SolidRenderTheme
import ch.bailu.foc.Foc
import ch.bailu.foc.FocFactory
import ch.bailu.foc.FocFile

class GtkMapDirectories(private val storageInterface: StorageInterface, private val focFactory: FocFactory): MapDirectories {

    companion object {
        const val mapChild = "maps"
    }

    override fun getWellKnownMapDirs(): ArrayList<Foc> {
        val solidGtkDefaultDirectory = SolidGtkDefaultDirectory(storageInterface, focFactory)
        val result = ArrayList<Foc>()

        solidGtkDefaultDirectory.buildSubDirectorySelection(ArrayList(), mapChild).forEach {
            result.add(FocFile(it))
        }
        return result
    }

    override fun getDefault(): Foc {
        return SolidGtkDefaultDirectory(storageInterface, focFactory).getValueAsFile().child(mapChild)
    }

    override fun createSolidDirectory(): SolidMapsForgeDirectory {
        return SolidMapsForgeDirectory(storageInterface, focFactory, this)
    }

    override fun createSolidFile(): SolidMapsForgeMapFile {
        return SolidMapsForgeMapFile(storageInterface, focFactory, this)
    }

    override fun createSolidRenderTheme(): SolidRenderTheme {
        return SolidRenderTheme(createSolidDirectory(), focFactory)
    }
}
