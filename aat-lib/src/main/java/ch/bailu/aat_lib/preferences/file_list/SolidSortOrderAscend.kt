package ch.bailu.aat_lib.preferences.file_list

import ch.bailu.aat_lib.preferences.SolidBoolean
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.resources.ToDo

class SolidSortOrderAscend(storage: StorageInterface, key: String) : SolidBoolean(storage, key) {

    override fun getLabel(): String {
        return ToDo.translate("Ascend")
    }
}
