package ch.bailu.aat_lib.api.poi

import ch.bailu.aat_lib.gpx.attributes.GpxAttributes
import ch.bailu.aat_lib.gpx.attributes.GpxAttributesStatic
import ch.bailu.aat_lib.gpx.attributes.Keys.Companion.toIndex
import ch.bailu.aat_lib.gpx.interfaces.GpxPointInterface
import ch.bailu.aat_lib.service.elevation.ElevationProvider
import org.mapsforge.core.model.Tag
import org.mapsforge.poi.storage.PointOfInterest


class GpxPointPoi(val poi: PointOfInterest) : GpxPointInterface {
    private val attributes: GpxAttributes = toAttributes(poi.tags)

    override fun getAltitude(): Float {
        if (attributes.hasKey(KEY_ELE)) return attributes[KEY_ELE].toFloat()

        if (attributes.hasKey(KEY_ALTITUDE)) return attributes[KEY_ALTITUDE].toFloat()

        return ElevationProvider.NULL_ALTITUDE
    }

    override fun getLongitude(): Double {
        return poi.longitude
    }

    override fun getLatitude(): Double {
        return poi.latitude
    }

    override fun getTimeStamp(): Long {
        return 0
    }

    override fun getAttributes(): GpxAttributes {
        return attributes
    }

    override fun getLatitudeE6(): Int {
        return (getLatitude() * 1e6).toInt()
    }

    override fun getLongitudeE6(): Int {
        return (getLongitude() * 1e6).toInt()
    }

    companion object {
        private val KEY_ELE = toIndex("ele")
        private val KEY_ALTITUDE = toIndex("altitude")

        private fun toAttributes(tags: Set<Tag>): GpxAttributes {
            val attributes = ArrayList<GpxAttributesStatic.Tag>()
            tags.forEach {
                attributes.add(GpxAttributesStatic.Tag(toIndex(it.key), it.value))
            }
            return GpxAttributesStatic(attributes.toTypedArray())
        }
    }
}
