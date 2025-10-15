package ch.bailu.aat_lib.preferences.map

import ch.bailu.aat_lib.coordinates.Dem3Coordinates
import ch.bailu.aat_lib.preferences.SolidFile
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.resources.Res
import ch.bailu.foc.Foc
import ch.bailu.foc.FocFactory


abstract class SolidDem3Directory(storage: StorageInterface, focFactory: FocFactory) :
    SolidFile(storage, SolidDem3Directory::class.java.simpleName, focFactory) {

    override fun getLabel(): String {
        return Res.str().p_dem_location()
    }

    override fun getValueAsString(): String {
        var r = super.getValueAsString()
        if (getStorage().isDefaultString(r)) {
            r = getDefaultValueFromSelection("")
            setValue(r)
        }
        return r
    }

    /**
     *
     * @return a complete file path for a dem3 tile. The base path is taken from configuration.
     * Example: /sdcard/aat_data/dem3/N16/N16E077.hgt.zip
     */
    fun toFile(dem3Coordinates: Dem3Coordinates): Foc {
        return toFile(dem3Coordinates, getValueAsFile())
    }

    private fun toFile(dem3Coordinates: Dem3Coordinates, base: Foc): Foc {
        return base.descendant(dem3Coordinates.toExtString() + ".hgt.zip")
    }

    companion object {
        const val DEM3_DIR = "dem3"
    }
}
