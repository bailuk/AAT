package ch.bailu.aat_lib.preferences.map

import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.resources.Res
import ch.bailu.foc.FocFactory


class SolidMapsForgeMapFile(storage: StorageInterface, factory: FocFactory, private val directories: MapDirectories) : SolidMapsForgeDirectory(storage, factory, directories) {

    override fun getLabel(): String {
        return Res.str().p_offline_map()
    }

    override fun buildSelection(list: ArrayList<String>): ArrayList<String> {
        val dirs = directories.getWellKnownMapDirs()
        for (dir in dirs) {
            addByExtension(list, dir, EXTENSION)
            addByExtensionIncludeSubdirectories(list, dir, EXTENSION)
        }
        return list
    }
}
