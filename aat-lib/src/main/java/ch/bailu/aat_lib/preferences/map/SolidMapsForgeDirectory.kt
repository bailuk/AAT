package ch.bailu.aat_lib.preferences.map

import ch.bailu.aat_lib.preferences.SelectionList
import ch.bailu.aat_lib.preferences.SolidFile
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.resources.Res
import ch.bailu.foc.Foc
import ch.bailu.foc.FocFactory
import javax.annotation.Nonnull

open class SolidMapsForgeDirectory(storage: StorageInterface, factory: FocFactory, private val directories: MapDirectories) : SolidFile(
    storage, KEY, factory) {

    @Nonnull
    override fun getLabel(): String {
        return Res.str().p_mapsforge_directory()
    }

    @Nonnull
    override fun getValueAsString(): String {
        var r = super.getValueAsString()
        if (getStorage().isDefaultString(r)) {
            r = getDefaultValue()
            setValue(r)
        }
        return r
    }

    private fun getDefaultValue(): String {
        var list = ArrayList<String>(5)
        list = buildSelection(list)
        val external = directories.default
        if (external != null) {
            SelectionList.add_w(list, external)
        }
        return if (list.size > 0) {
            list[0]
        } else ""
    }

    override fun buildSelection(list: ArrayList<String>): ArrayList<String> {
        val dirs = directories.wellKnownMapDirs
        for (dir in dirs) {
            SelectionList.add_r(list, dir)
            SelectionList.add_subdirectories_r(list, dir)
        }
        return list
    }

    val wellKnownMapDirs: ArrayList<Foc>
        get() = directories.wellKnownMapDirs

    companion object {
        const val EXTENSION = ".map"
        const val MAPS_DIR = "maps"
        const val ORUX_MAPS_DIR = "oruxmaps/mapfiles"
        private val KEY = SolidMapsForgeDirectory::class.java.simpleName
    }
}
