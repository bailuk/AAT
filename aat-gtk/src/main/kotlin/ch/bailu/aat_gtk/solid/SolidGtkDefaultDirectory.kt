package ch.bailu.aat_gtk.solid

import ch.bailu.aat_lib.factory.FocFactory
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.system.SolidDataDirectoryDefault
import java.util.ArrayList

class SolidGtkDefaultDirectory (storage: StorageInterface, focFactory: FocFactory) :
    SolidDataDirectoryDefault(storage, focFactory) {

    override fun buildSelection(list: ArrayList<String>): ArrayList<String> {
        val home = System.getProperty("user.home")
        list.add("${home}/aat_data")
        return list
    }
}
