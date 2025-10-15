package ch.bailu.aat_lib.preferences.map

import ch.bailu.aat_lib.preferences.SolidFile
import ch.bailu.aat_lib.resources.Res
import ch.bailu.foc.FocFactory

class SolidPoiDatabase(private val directories: SolidMapsForgeDirectoryHint, factory: FocFactory) :
    SolidFile(directories.getStorage(), KEY, factory
) {

    override fun getLabel(): String {
        return Res.str().p_mapsforge_poi_db()
    }

    override fun buildSelection(list: ArrayList<String>): ArrayList<String> {
        for (dir in directories.getKnownMapDirs()) {
            addByExtension(list, dir, EXTENSION)
            addByExtensionIncludeSubdirectories(list, dir, EXTENSION)
        }
        return list
    }

    override fun getPatterns(): Array<String> {
        return arrayOf("*${EXTENSION}", ALL_PATTERN)
    }

    companion object {
        const val KEY = "SolidPoiDatabase"
        private const val EXTENSION = ".poi"
    }
}
