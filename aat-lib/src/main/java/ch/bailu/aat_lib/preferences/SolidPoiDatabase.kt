package ch.bailu.aat_lib.preferences

import ch.bailu.aat_lib.preferences.map.SolidMapsForgeDirectory
import ch.bailu.aat_lib.resources.Res
import ch.bailu.foc.FocFactory
import javax.annotation.Nonnull

class SolidPoiDatabase(private val smapDirectory: SolidMapsForgeDirectory, focFactory: FocFactory) : SolidFile(
    smapDirectory.getStorage(), SolidPoiDatabase::class.java.simpleName, focFactory
) {
    @Nonnull
    override fun getLabel(): String {
        return Res.str().p_mapsforge_poi_db()
    }

    override fun buildSelection(list: ArrayList<String>): ArrayList<String> {
        val dirs = smapDirectory.wellKnownMapDirs
        for (dir in dirs) {
            addByExtension(list, dir, EXTENSION)
            addByExtensionIncludeSubdirectories(list, dir, EXTENSION)
        }
        return list
    }

    companion object {
        private const val EXTENSION = ".poi"
    }
}
