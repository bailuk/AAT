package ch.bailu.aat_gtk.solid

import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.map.SolidDem3Directory
import ch.bailu.foc.FocFactory

class GtkSolidDem3Directory(storage: StorageInterface, private val focFactory: FocFactory) : SolidDem3Directory(storage, focFactory) {
    override fun buildSelection(list: ArrayList<String>): ArrayList<String> {
        val solidGtkDefaultDirectory = SolidGtkDefaultDirectory(getStorage(), focFactory)
        return solidGtkDefaultDirectory.buildSubDirectorySelection(list, "dem3")
    }
}
