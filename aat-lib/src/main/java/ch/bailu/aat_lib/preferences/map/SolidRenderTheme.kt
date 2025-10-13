package ch.bailu.aat_lib.preferences.map

import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.preferences.SolidFile
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.util.extensions.getFirstOrDefault
import ch.bailu.foc.FocFactory
import org.mapsforge.map.rendertheme.ExternalRenderTheme
import org.mapsforge.map.rendertheme.XmlRenderTheme
import org.mapsforge.map.rendertheme.internal.MapsforgeThemes
import java.io.File
import java.io.FileNotFoundException


class SolidRenderTheme(private val directories: SolidMapsForgeDirectoryHint, factory: FocFactory)
    : SolidFile(directories.getStorage(), KEY, factory) {

    override fun getLabel(): String {
        return Res.str().p_mapsforge_theme()
    }

    val valueAsRenderTheme: XmlRenderTheme
        get() = toRenderTheme(getValueAsString())
    val valueAsThemeID: String
        get() = toThemeID(getValueAsString())
    val valueAsThemeName: String
        get() = toThemeName(valueAsThemeID)

    override fun getValueAsString(): String {
        var r = super.getValueAsString()
        if (getStorage().isDefaultString(r)) {
            r = getDefaultValue()
            setValue(r)
        }
        return r
    }

    private fun getDefaultValue(): String {
        var list = ArrayList<String>()
        list = buildSelection(list)
        return list.getFirstOrDefault("")
    }

    override fun buildSelection(list: ArrayList<String>): ArrayList<String> {
        MapsforgeThemes.entries.forEach {
            list.add(it.name)
        }

        for (dir in directories.getKnownMapDirs()) {
            addByExtension(list, dir, EXTENSION)
            addByExtensionIncludeSubdirectories(list, dir, EXTENSION)
        }

        return list
    }

    companion object {
        private const val KEY = "SolidRenderTheme"
        private const val EXTENSION = ".xml"

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
