package ch.bailu.aat_gtk.solid

import ch.bailu.aat_gtk.config.Environment
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.system.SolidDataDirectoryDefault
import ch.bailu.aat_lib.util.extensions.addUnique
import ch.bailu.foc.FocFactory

class SolidGtkDefaultDirectory (storage: StorageInterface, focFactory: FocFactory) :
    SolidDataDirectoryDefault(storage, focFactory) {

    override fun buildSelection(list: ArrayList<String>): ArrayList<String> {
        list.addUnique(Environment.dataHome)
        list.addUnique(Environment.configHome)
        list.addUnique(Environment.cacheHome)
        return list
    }

    fun buildSubDirectorySelection(result: ArrayList<String>, subDirectory: String): ArrayList<String> {
        result.addUnique("${getValueAsString()}/${subDirectory}")
        buildSelection().forEach {
            result.addUnique("${it}/${subDirectory}")
        }
        return result
    }
}
