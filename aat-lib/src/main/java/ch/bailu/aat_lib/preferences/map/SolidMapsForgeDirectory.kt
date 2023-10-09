package ch.bailu.aat_lib.preferences.map

import ch.bailu.aat_lib.preferences.SelectionList
import ch.bailu.aat_lib.preferences.SolidFile
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.resources.Res
import ch.bailu.foc.Foc
import ch.bailu.foc.FocFactory


open class SolidMapsForgeDirectory(storage: StorageInterface, factory: FocFactory, private val directories: MapDirectories) : SolidFile(
    storage, KEY, factory) {


    override fun getLabel(): String {
        return Res.str().p_mapsforge_directory()
    }


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

        val external = directories.getDefault()
        if (external is Foc) {
            SelectionList.addW(list, external)
        }

        return if (list.size > 0) {
            list[0]
        } else ""
    }

    override fun buildSelection(list: ArrayList<String>): ArrayList<String> {
        val dirs = directories.getWellKnownMapDirs()
        for (dir in dirs) {
            SelectionList.addR(list, dir)
            SelectionList.addSubdirectoriesR(list, dir)
        }
        return list
    }

    val wellKnownMapDirs: ArrayList<Foc>
        get() = directories.getWellKnownMapDirs()

    companion object {
        const val EXTENSION = ".map"
        const val MAPS_DIR = "maps"
        const val ORUX_MAPS_DIR = "oruxmaps/mapfiles"
        private val KEY = SolidMapsForgeDirectory::class.java.simpleName
    }
}
