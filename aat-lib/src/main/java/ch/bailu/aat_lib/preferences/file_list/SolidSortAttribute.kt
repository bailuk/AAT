package ch.bailu.aat_lib.preferences.file_list

import ch.bailu.aat_lib.preferences.SolidIndexList
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.resources.ToDo
import ch.bailu.aat_lib.service.directory.database.GpxDbConfiguration

class SolidSortAttribute(storage: StorageInterface, key: String) : SolidIndexList(storage, key) {

    companion object {
        private val label = arrayOf (
            Res.str().d_startdate(),
            Res.str().name(),
            Res.str().distance(),
            Res.str().time(),
            Res.str().d_enddate(),
            Res.str().speed(),
            Res.str().maximum(),
            Res.str().pause()
        )

        private val attribute = arrayOf (
            GpxDbConfiguration.ATTR_START_TIME,
            GpxDbConfiguration.ATTR_FILENAME,
            GpxDbConfiguration.ATTR_DISTANCE,
            GpxDbConfiguration.ATTR_TOTAL_TIME,
            GpxDbConfiguration.ATTR_END_TIME,
            GpxDbConfiguration.ATTR_AVG_SPEED,
            GpxDbConfiguration.ATTR_MAX_SPEED,
            GpxDbConfiguration.ATTR_PAUSE,
        )
    }

    override fun length(): Int {
        return label.size
    }

    override fun getValueAsString(index: Int): String {
        return label[validate(index)]
    }

    fun getAttributeName(): String {
        return attribute[validate(index)]
    }

    override fun getLabel(): String {
        return ToDo.translate("Sort by...")
    }
}
