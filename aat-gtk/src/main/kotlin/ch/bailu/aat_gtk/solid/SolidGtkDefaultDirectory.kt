package ch.bailu.aat_gtk.solid

import ch.bailu.aat_gtk.config.Environment
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.system.SolidDataDirectoryDefault
import ch.bailu.aat_lib.util.fs.AppDirectory
import ch.bailu.foc.FocFactory

class SolidGtkDefaultDirectory (storage: StorageInterface, focFactory: FocFactory) :
    SolidDataDirectoryDefault(storage, focFactory) {

    override fun buildSelection(list: ArrayList<String>): ArrayList<String> {
        addDistinct(list,"${Environment.configDirectory}")
        addDistinct(list,"${Environment.userHome}/${AppDirectory.DIR_AAT_DATA}")
        return list
    }

    fun buildSubDirectorySelection(result: ArrayList<String>, subDirectory: String): ArrayList<String> {
        addDistinct(result, "${getValueAsString()}/${subDirectory}")
        buildSelection(ArrayList()).forEach {
            addDistinct(result, "${it}/${subDirectory}")
        }
        return result
    }

    private fun addDistinct(list: ArrayList<String>, string: String) {
        if (!list.contains(string)) {
            list.add(string)
        }
    }
}
