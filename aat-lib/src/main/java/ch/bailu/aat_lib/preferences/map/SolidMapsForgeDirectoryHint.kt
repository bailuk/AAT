package ch.bailu.aat_lib.preferences.map

import ch.bailu.aat_lib.preferences.SelectionList
import ch.bailu.aat_lib.preferences.SolidFile
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.util.extensions.getFirstOrDefault
import ch.bailu.foc.Foc
import ch.bailu.foc.FocFactory


class SolidMapsForgeDirectoryHint(storage: StorageInterface, factory: FocFactory, private val directories: MapDirectories) : SolidFile(
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
        return list.getFirstOrDefault("")
    }

    override fun buildSelection(list: ArrayList<String>): ArrayList<String> {
        val dirs = directories.getWellKnownMapDirs()
        for (dir in dirs) {
            SelectionList.addR(list, dir)
        }
        return list
    }

    fun getKnownMapDirs(): ArrayList<Foc> {
        val result = directories.getWellKnownMapDirs()
        if (!result.contains(getValueAsFile())) {
            result.add(0, getValueAsFile())
        }
        return result
    }

    companion object {
        const val KEY = "SolidMapsForgeDirectoryHint"
    }
}
