package ch.bailu.aat_lib.service.directory

import ch.bailu.aat_lib.coordinates.BoundingBoxE6
import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.gpx.interfaces.GpxType
import ch.bailu.aat_lib.gpx.interfaces.GpxType.Companion.fromInteger
import ch.bailu.aat_lib.service.directory.database.GpxDbConfiguration
import ch.bailu.aat_lib.util.sql.DbResultSet
import ch.bailu.foc.Foc

class GpxInformationDbEntry(private val cursor: DbResultSet, private val parent: Foc) :
    GpxInformation() {
    override fun getLoaded(): Boolean {
        return false //isSupported();
    }

    override fun getFile(): Foc {
        val name = getString(GpxDbConfiguration.ATTR_FILENAME)
        return parent.child(name)
    }

    override fun getSpeed(): Float {
        return getFloat(GpxDbConfiguration.ATTR_AVG_SPEED)
    }

    override fun getDistance(): Float {
        return getFloat(GpxDbConfiguration.ATTR_DISTANCE)
    }

    override fun getPause(): Long {
        return getLong(GpxDbConfiguration.ATTR_PAUSE)
    }

    val isValid: Boolean
        get() = (!cursor.isClosed() && cursor.getPosition() > -1 && cursor.getPosition() < cursor.getCount())

    private fun getString(key: String): String {
        if (isValid) {
            return cursor.getString(key)
        }
        return ""
    }

    private fun getLong(key: String): Long {
        if (isValid) {
            return cursor.getLong(key)
        }
        return 0
    }


    private fun getFloat(key: String): Float {
        if (isValid) {
            return cursor.getFloat(key)
        }
        return 0f
    }

    override fun getTimeStamp(): Long {
        return getStartTime()
    }

    override fun getStartTime(): Long {
        return getLong(GpxDbConfiguration.ATTR_START_TIME)
    }

    override fun getTimeDelta(): Long {
        return getLong(GpxDbConfiguration.ATTR_TOTAL_TIME)
    }

    override fun getEndTime(): Long {
        return getLong(GpxDbConfiguration.ATTR_END_TIME)
    }

    override fun getBoundingBox(): BoundingBoxE6 {
        return BoundingBoxE6(
            getLong(GpxDbConfiguration.ATTR_NORTH_BOUNDING).toInt(),
            getLong(GpxDbConfiguration.ATTR_EAST_BOUNDING).toInt(),
            getLong(GpxDbConfiguration.ATTR_SOUTH_BOUNDING).toInt(),
            getLong(GpxDbConfiguration.ATTR_WEST_BOUNDING).toInt()
        )
    }

    override fun getType(): GpxType {
        val id = getLong(GpxDbConfiguration.ATTR_TYPE_ID).toInt()
        return fromInteger(id)
    }
}
