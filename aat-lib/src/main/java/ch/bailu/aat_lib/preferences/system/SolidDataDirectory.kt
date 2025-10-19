package ch.bailu.aat_lib.preferences.system

import ch.bailu.aat_lib.preferences.SolidFile
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.util.extensions.addUnique
import ch.bailu.foc.FocFactory

open class SolidDataDirectory(storageInterface: StorageInterface, focFactory: FocFactory)
    : SolidFile(storageInterface, SolidDataDirectory::class.java.simpleName, focFactory) {

    override fun getLabel(): String {
        return Res.str().p_directory_data()
    }

    override fun getValueAsString(): String {
        val r = super.getValueAsString()
        return if (getStorage().isDefaultString(r)) setDefaultValue() else r
    }

    fun buildSubDirectorySelection(result: ArrayList<String>, subDirectory: String): ArrayList<String> {
        result.addUnique("${getValueAsString()}/${subDirectory}")
        buildSelection().forEach {
            result.addUnique("${it}/${subDirectory}")
        }
        return result
    }
}
