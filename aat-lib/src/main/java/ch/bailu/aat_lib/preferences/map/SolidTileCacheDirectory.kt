package ch.bailu.aat_lib.preferences.map

import ch.bailu.aat_lib.preferences.SolidFile
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.resources.Res
import ch.bailu.foc.FocFactory


abstract class SolidTileCacheDirectory(storage: StorageInterface, focFactory: FocFactory) : SolidFile(
    storage, SolidTileCacheDirectory::class.java.simpleName, focFactory
) {

    override fun getLabel(): String {
        return Res.str().p_directory_tiles()
    }


    override fun getValueAsString(): String {
        var r = super.getValueAsString()
        if (getStorage().isDefaultString(r)) {
            r = getDefaultValueFromSelection()
            setValue(r)
        }
        return r
    }
}
