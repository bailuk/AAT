package ch.bailu.aat_gtk.preferences

import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.map.MapDirectories
import ch.bailu.aat_lib.preferences.map.SolidMapsForgeDirectoryHint
import ch.bailu.aat_lib.preferences.map.SolidMapsForgeMapFile
import ch.bailu.aat_lib.preferences.map.SolidRenderTheme
import ch.bailu.aat_lib.util.extensions.addUnique
import ch.bailu.foc.Foc
import ch.bailu.foc.FocFactory
import ch.bailu.foc.FocFile

class GtkMapDirectories(private val storageInterface: StorageInterface, private val focFactory: FocFactory): MapDirectories {

    companion object {
        const val MAP_CHILD = "maps"
    }

    override fun getWellKnownMapDirs(): ArrayList<Foc> {
        val solidGtkDefaultDirectory = GtkSolidDataDirectory(storageInterface, focFactory)
        val result = ArrayList<Foc>()

        solidGtkDefaultDirectory.buildSubDirectorySelection(ArrayList(), MAP_CHILD).forEach {
            result.addUnique(FocFile(it))
        }
        return result
    }

    override fun getDefault(): Foc {
        return GtkSolidDataDirectory(storageInterface, focFactory).getValueAsFile().child(MAP_CHILD)
    }

    override fun createSolidDirectory(): SolidMapsForgeDirectoryHint {
        return SolidMapsForgeDirectoryHint(storageInterface, focFactory, this)
    }

    override fun createSolidFile(): SolidMapsForgeMapFile {
        return SolidMapsForgeMapFile(createSolidDirectory(), focFactory)
    }

    override fun createSolidRenderTheme(): SolidRenderTheme {
        return SolidRenderTheme(createSolidDirectory(), focFactory)
    }
}
