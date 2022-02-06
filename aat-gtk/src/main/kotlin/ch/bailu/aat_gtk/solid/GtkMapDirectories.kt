package ch.bailu.aat_gtk.solid

import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.map.MapDirectories
import ch.bailu.foc.Foc
import ch.bailu.foc.FocFactory
import java.util.ArrayList

class GtkMapDirectories(private val storageInterface: StorageInterface, private val focFactory:FocFactory): MapDirectories {

    override fun getWellKnownMapDirs(): ArrayList<Foc> {
        val result = ArrayList<Foc>()
        result.add(default)
        return result
    }

    override fun getDefault(): Foc {
        return SolidGtkDefaultDirectory(storageInterface, focFactory).valueAsFile.child("maps")
    }
}