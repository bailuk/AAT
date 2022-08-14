package ch.bailu.aat_gtk.solid

import ch.bailu.aat_gtk.config.Strings.appIdName
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.system.SolidDataDirectoryDefault
import ch.bailu.aat_lib.util.fs.AppDirectory
import ch.bailu.foc.FocFactory
import java.util.ArrayList

class SolidGtkDefaultDirectory (storage: StorageInterface, focFactory: FocFactory) :
    SolidDataDirectoryDefault(storage, focFactory) {

    override fun buildSelection(list: ArrayList<String>): ArrayList<String> {
        val home = System.getProperty("user.home")
        list.add("$home/.config/$appIdName")
        list.add("$home/${AppDirectory.DIR_AAT_DATA}")
        return list
    }
}
