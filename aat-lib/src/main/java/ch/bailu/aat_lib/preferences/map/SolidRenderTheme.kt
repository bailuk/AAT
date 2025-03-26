package ch.bailu.aat_lib.preferences.map

import ch.bailu.aat_lib.preferences.SolidFile
import ch.bailu.aat_lib.resources.Res
import ch.bailu.foc.FocFactory
import org.mapsforge.map.rendertheme.ExternalRenderTheme
import org.mapsforge.map.rendertheme.XmlRenderTheme
import org.mapsforge.map.rendertheme.internal.MapsforgeThemes
import java.io.File
import java.io.FileNotFoundException


class SolidRenderTheme(private val mapsForgeDirectory: SolidMapsForgeDirectory, focFactory: FocFactory)
    : SolidFile(mapsForgeDirectory.getStorage(), SolidRenderTheme::class.java.simpleName, focFactory) {

    override fun getLabel(): String {
        return Res.str().p_mapsforge_theme()
    }

    val valueAsRenderTheme: XmlRenderTheme
        get() = toRenderTheme(getValueAsString())
    val valueAsThemeID: String
        get() = toThemeID(getValueAsString())
    val valueAsThemeName: String
        get() = toThemeName(valueAsThemeID)

    override fun buildSelection(list: ArrayList<String>): ArrayList<String> {
        list.add(MapsforgeThemes.DEFAULT.toString())
        list.add(MapsforgeThemes.OSMARENDER.toString())
        val maps = mapsForgeDirectory.getValueAsFile()
        addByExtension(list, maps, EXTENSION)
        addByExtensionIncludeSubdirectories(list, maps, EXTENSION)
        val dirs = mapsForgeDirectory.wellKnownMapDirs
        for (dir in dirs) {
            addByExtension(list, dir, EXTENSION)
            addByExtensionIncludeSubdirectories(list, dir, EXTENSION)
        }
        return list
    }

    companion object {
        private const val EXTENSION = ".xml"
        @JvmStatic
        fun toThemeName(themeFile: String): String {
            return File(themeFile).name.replace(EXTENSION, "")
        }

        private fun toThemeID(name: String): String {
            return if (name == MapsforgeThemes.DEFAULT.toString()) {
                name
            } else if (name == MapsforgeThemes.OSMARENDER.toString()) {
                name
            } else {
                try {
                    ExternalRenderTheme(File(name))
                    name
                } catch (e1: FileNotFoundException) {
                    MapsforgeThemes.DEFAULT.toString()
                }
            }
        }

        fun toRenderTheme(name: String): XmlRenderTheme {
            return if (name == MapsforgeThemes.DEFAULT.toString()) {
                MapsforgeThemes.DEFAULT
            } else if (name == MapsforgeThemes.OSMARENDER.toString()) {
                MapsforgeThemes.OSMARENDER
            } else {
                try {
                    ExternalRenderTheme(File(name))
                } catch (e1: FileNotFoundException) {
                    MapsforgeThemes.DEFAULT
                }
            }
        }
    }
}
