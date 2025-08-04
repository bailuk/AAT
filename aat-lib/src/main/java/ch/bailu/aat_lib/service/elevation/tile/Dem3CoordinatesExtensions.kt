package ch.bailu.aat_lib.service.elevation.tile

import ch.bailu.aat_lib.coordinates.Dem3Coordinates
import ch.bailu.aat_lib.service.elevation.tile.Dem3CoordinatesExtensions.REF_LO_1
import ch.bailu.aat_lib.service.elevation.tile.Dem3CoordinatesExtensions.REF_LO_2
import org.mapsforge.core.model.LatLong
import org.mapsforge.core.util.LatLongUtils

private object Dem3CoordinatesExtensions {
    const val REF_LO_1 = 7.0
    const val REF_LO_2 = 8.0
}

fun Dem3Coordinates.getCellDistance(): Float {
    val fDistance = LatLongUtils.sphericalDistance(
        LatLong(getLatitudeE6() / 1e6, REF_LO_1),
        LatLong(getLatitudeE6() / 1e6, REF_LO_2)
    ).toFloat()

    val iDistance = fDistance / (Dem3Array.dem3Dimension.dimension - Dem3Array.dem3Dimension.offset)

    return if (iDistance == 0f) {
        50f
    } else {
        iDistance
    }
}

fun Dem3Coordinates.hasInverseLatitude(): Boolean {
    return getLatitudeE6() > 0
}

fun Dem3Coordinates.hasInverseLongitude(): Boolean {
    return getLongitudeE6() < 0
}
