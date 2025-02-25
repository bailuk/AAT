package ch.bailu.aat_lib.preferences

import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.service.directory.database.GpxDbConfiguration
import ch.bailu.foc.FocFactory

class SolidDirectoryQuery(storage: StorageInterface, focFactory: FocFactory) : SolidFile(storage, KEY_DIR_DIRECTORY, focFactory) {

    fun containsKey(key: String): Boolean {
        return key.contains(getValueAsString())
    }

    val position: SolidInteger
        get() = SolidInteger(getStorage(), KEY_DIR_INDEX + getValueAsString())

    val useDateStart: SolidBoolean
        get() = SolidBoolean(getStorage(), KEY_USE_DATE_START + getValueAsString())

    val useDateEnd: SolidBoolean
        get() = SolidBoolean(getStorage(), KEY_USE_DATE_END + getValueAsString())

    val dateStart: SolidDate
        get() = SolidDate(getStorage(),KEY_DATE_START + getValueAsString(), Res.str().filter_date_start())

    val dateTo: SolidDate
        get() = SolidDate(getStorage(),KEY_DATE_END + getValueAsString(), Res.str().filter_date_to())

    val useGeo: SolidBoolean
        get() = SolidBoolean(getStorage(), KEY_USE_GEO + getValueAsString())

    val boundingBox: SolidBoundingBox
        get() = SolidBoundingBox(getStorage(),KEY_BOUNDING_BOX + getValueAsString(), Res.str().filter_geo())

    fun createSelectionString(): String {
        val d = createSelectionStringDate()
        val b = createSelectionStringBounding()
        return if (d.isNotEmpty() && b.isNotEmpty()) {
            "($d) AND ($b)"
        } else {
            d + b
        }
    }

    private fun createSelectionStringBounding(): String {
        return if (useGeo.value) {
            boundingBox.createSelectionStringInside()
        } else ""
    }

    private fun createSelectionStringDate(): String {
        var selection = ""
        if (useDateStart.value || useDateEnd.value) {
            var end: Long
            val start: Long = if (useDateStart.value) {
                dateStart.getValue()
            } else {
                0
            }
            if (useDateEnd.value) {
                end = dateTo.getValue()
            } else {
                end = System.currentTimeMillis() / DAY
                end += 5
                end *= DAY
            }
            selection = (GpxDbConfiguration.KEY_START_TIME
                    + " BETWEEN "
                    + Math.min(start, end)
                    + " AND "
                    + Math.max(start, end))
        }
        return selection
    }

    companion object {
        private const val KEY_DIR_DIRECTORY = "DIR_DIRECTORY"
        private const val KEY_DIR_INDEX = "DIR_INDEX_"
        private const val KEY_DATE_START = "DATE_START_"
        private const val KEY_DATE_END = "DATE_END_"
        private const val KEY_USE_DATE_START = "USE_DATE_START_"
        private const val KEY_USE_DATE_END = "USE_DATE_END_"
        private const val KEY_USE_GEO = "USE_GEO_"
        private const val KEY_BOUNDING_BOX = "BOX_"
        private const val DAY = (1000 * 60 * 60 * 24).toLong() // /* ms*sec*min*h = d
    }
}
