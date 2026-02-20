package ch.bailu.aat_gtk.app

import ch.bailu.aat_lib.api.cm.CmApi
import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.gpx.information.InformationUtil.overlayInfoIdList

object GtkInformationUtil {
    val mapOverlayInfoIdList: List<Int> = ArrayList<Int>().apply {
        addAll(overlayInfoIdList)
        add(InfoID.POI)
        add(InfoID.EDITOR_DRAFT)
        add(InfoID.FILE_VIEW)
        add(InfoID.EDITOR_OVERLAY)
        add(InfoID.TRACKER)
        add(InfoID.LIST_SUMMARY)
        add(InfoID.NOMINATIM_REVERSE)
        add(InfoID.BROUTER)
        if (CmApi.ENABLED) {
            add(InfoID.CRITICAL_MAP)
        }
    }
}
