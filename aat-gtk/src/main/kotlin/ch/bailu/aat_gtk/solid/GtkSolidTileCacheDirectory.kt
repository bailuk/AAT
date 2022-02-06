package ch.bailu.aat_gtk.solid

import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.map.SolidTileCacheDirectory
import ch.bailu.foc.FocFactory
import java.util.ArrayList

class GtkSolidTileCacheDirectory(storage: StorageInterface,
                                 private val focFactory: FocFactory
) : SolidTileCacheDirectory(storage, focFactory) {

    override fun buildSelection(list: ArrayList<String>): ArrayList<String> {
        list.add("${SolidGtkDefaultDirectory(storage, focFactory).valueAsString}/tile_cache")
        return list
    }
}