package ch.bailu.aat_lib.service.directory

import ch.bailu.aat_lib.gpx.GpxBigDelta
import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.gpx.GpxPoint
import ch.bailu.aat_lib.gpx.attributes.GpxAttributes
import ch.bailu.aat_lib.gpx.attributes.GpxAttributesNull
import ch.bailu.aat_lib.gpx.attributes.GpxListAttributes.Companion.factoryTrackList
import ch.bailu.aat_lib.gpx.attributes.MaxSpeed
import ch.bailu.aat_lib.gpx.attributes.MaxSpeed.Raw2
import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.gpx.interfaces.GpxType
import ch.bailu.aat_lib.util.sql.DbResultSet
import ch.bailu.foc.Foc


class GpxInformationDbSummary(private val directory: Foc, cursor: DbResultSet) : GpxInformation() {
    private val list = GpxList(GpxType.WAY, factoryTrackList())

    private val maxSpeed: MaxSpeed = Raw2()


    init {
        val summary = GpxBigDelta(factoryTrackList())
        val entry: GpxInformation = GpxInformationDbEntry(cursor, directory)

        cursor.moveToPosition(-1)
        while (cursor.moveToNext()) {
            addEntryToList(entry)
            if (hasTimeDelta(entry)) {
                summary.updateWithPause(entry)
            }
        }
        setVisibleTrackSegment(summary)
    }

    private fun hasTimeDelta(entry: GpxInformation): Boolean {
        return entry.getTimeDelta() > 0 && entry.getStartTime() > 0 && entry.getEndTime() > entry.getStartTime()
    }

    private fun addEntryToList(entry: GpxInformation) {
        val point = GpxPoint(
            entry.getBoundingBox().center,
            0f, entry.getTimeStamp()
        )

        list.appendToCurrentSegment(point, GpxAttributesNull.NULL)
        maxSpeed.add(entry.getSpeed())
    }

    override fun getFile(): Foc {
        return directory
    }

    override fun getGpxList(): GpxList {
        return list
    }

    override fun getLoaded(): Boolean {
        return true
    }

    fun getID(): Int {
        return InfoID.LIST_SUMMARY
    }

    override fun getAttributes(): GpxAttributes {
        return maxSpeed
    }
}
