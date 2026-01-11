package ch.bailu.aat.app

import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.gpx.information.InformationUtil.overlayInfoIdList

object AndroidInformationUtil {
    val mapOverlayInfoIdList: List<Int> = ArrayList<Int>().apply {
        addAll(overlayInfoIdList)
        add(InfoID.POI)
        add(InfoID.EDITOR_DRAFT)
        add(InfoID.TRACKER)
        add(InfoID.NOMINATIM)
        add(InfoID.NOMINATIM_REVERSE)
        add(InfoID.BROUTER)
        add(InfoID.OVERPASS)
    }
}
