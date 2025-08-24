package ch.bailu.aat_lib.preferences.file_list

import ch.bailu.aat_lib.preferences.SolidIndexList
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.resources.ToDo
import ch.bailu.aat_lib.service.directory.database.GpxDbConfiguration

class SolidSortAttribute(storage: StorageInterface, key: String) : SolidIndexList(storage, key) {

    companion object {
        private val label = arrayOf (
            Res.str().name(),
            Res.str().distance(),
            Res.str().d_startdate(),
            Res.str().time(),
        )

        private val sql = arrayOf (
            ToDo.translate("ORDER BY ${GpxDbConfiguration.ATTR_FILENAME}"),
            ToDo.translate("ORDER BY ${GpxDbConfiguration.ATTR_START_TIME}"),
            ToDo.translate("ORDER BY ${GpxDbConfiguration.ATTR_DISTANCE}"),
            ToDo.translate("ORDER BY ${GpxDbConfiguration.ATTR_TOTAL_TIME}"),
        )
    }

    override fun length(): Int {
        return label.size
    }

    override fun getValueAsString(index: Int): String {
        return label[validate(index)]
    }

    fun getSqlStatement(): String {
        return sql[validate(index)]
    }

    override fun getLabel(): String {
        return ToDo.translate("Sort by...")
    }
}
