package ch.bailu.aat_lib.preferences.map

import ch.bailu.aat_lib.preferences.SolidBoolean
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.resources.Res


class SolidDem3EnableDownload(storage: StorageInterface) : SolidBoolean(
    storage, SolidDem3EnableDownload::class.java.simpleName
) {
    
    override fun getLabel(): String {
        return Res.str().p_dem_auto_download()
    }
}
