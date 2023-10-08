package ch.bailu.aat_lib.preferences.map

import ch.bailu.aat_lib.preferences.SolidFile
import ch.bailu.aat_lib.resources.Res
import ch.bailu.foc.FocFactory
import org.mapsforge.map.rendertheme.ExternalRenderTheme
import org.mapsforge.map.rendertheme.InternalRenderTheme
import org.mapsforge.map.rendertheme.XmlRenderTheme
import java.io.File
import java.io.FileNotFoundException
import javax.annotation.Nonnull

class SolidRenderTheme(private val mapsForgeDirectory: SolidMapsForgeDirectory, focFactory: FocFactory)
    : SolidFile(mapsForgeDirectory.getStorage(), SolidRenderTheme::class.java.simpleName, focFactory) {
    @Nonnull
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
        list.add(InternalRenderTheme.DEFAULT.toString())
        list.add(InternalRenderTheme.OSMARENDER.toString())
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
            return if (name == InternalRenderTheme.DEFAULT.toString()) {
                name
            } else if (name == InternalRenderTheme.OSMARENDER.toString()) {
                name
            } else {
                try {
                    ExternalRenderTheme(File(name))
                    name
                } catch (e1: FileNotFoundException) {
                    InternalRenderTheme.DEFAULT.toString()
                }
            }
        }

        fun toRenderTheme(name: String): XmlRenderTheme {
            return if (name == InternalRenderTheme.DEFAULT.toString()) {
                InternalRenderTheme.DEFAULT
            } else if (name == InternalRenderTheme.OSMARENDER.toString()) {
                InternalRenderTheme.OSMARENDER
            } else {
                try {
                    ExternalRenderTheme(File(name))
                } catch (e1: FileNotFoundException) {
                    InternalRenderTheme.DEFAULT
                }
            }
        }
    }
}
