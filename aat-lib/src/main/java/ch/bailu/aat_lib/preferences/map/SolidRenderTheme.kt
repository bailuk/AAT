package ch.bailu.aat_lib.preferences.map

import ch.bailu.aat_lib.logger.AppLog
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
        MapsforgeThemes.entries.forEach {
            list.add(it.name)
        }

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
            if (name.trim().isNotEmpty()) {
                MapsforgeThemes.entries.forEach {
                    if (name == it.name) {
                        return name
                    }
                }
                try {
                    ExternalRenderTheme(File(name))
                    return name
                } catch (e: FileNotFoundException) {
                    AppLog.d(SolidRenderTheme::class.java.simpleName, e.message)
                }
            }
            return MapsforgeThemes.DEFAULT.toString()
        }

        fun toRenderTheme(name: String): XmlRenderTheme {
            MapsforgeThemes.entries.forEach {
                if (name == it.name) {
                    return it
                }
            }

            try {
                return ExternalRenderTheme(File(name))
            } catch (e: FileNotFoundException) {
                AppLog.w(this, e)
            }
            return MapsforgeThemes.DEFAULT
        }
    }
}
