package ch.bailu.aat_lib.gpx.information

import ch.bailu.aat_lib.preferences.map.SolidCustomOverlayList
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.resources.ToDo

object InformationUtil {

    fun defaultName(iid: Int): String {
        return if (iid == InfoID.POI) {
            Res.str().p_mapsforge_poi()
        } else if (iid == InfoID.EDITOR_DRAFT) {
            ToDo.translate("Draft")
        } else if (iid == InfoID.TRACKER) {
            Res.str().tracker()
        } else if (isOverlay(iid)) {
            "${ToDo.translate("Overlay")} ${getOverlayIndex(iid)}"
        } else if (iid == InfoID.NOMINATIM) {
            "Nominatim"
        } else if (iid == InfoID.OVERPASS) {
            Res.str().query_overpass()
        } else if (iid == InfoID.EDITOR_OVERLAY) {
            ToDo.translate("Editor")
        } else if (iid == InfoID.FILE_VIEW) {
            ToDo.translate("Selected File")
        } else if (iid == InfoID.LIST_SUMMARY) {
            ToDo.translate("List Summary")
        } else {
            ""
        }
    }

    private fun isOverlay(iid: Int): Boolean {
        return iid >= InfoID.OVERLAY && iid < InfoID.OVERLAY + SolidCustomOverlayList.MAX_OVERLAYS
    }

    private fun getOverlayIndex(iid: Int): Int {
        return iid - InfoID.OVERLAY + 1
    }


    fun getMapOverlayInfoIdList(): List<Int> {
        return ArrayList<Int>().apply {
            addAll(getOverlayInfoIdList())
            add(InfoID.POI)
            add(InfoID.EDITOR_DRAFT)
            add(InfoID.FILE_VIEW)
            add(InfoID.EDITOR_OVERLAY)
            add(InfoID.TRACKER)
            add(InfoID.LIST_SUMMARY)
        }
    }

    fun getEditableOverlayInfoIdList(): List<Int> {
        return ArrayList<Int>().apply {
            add(InfoID.EDITOR_DRAFT)
            add(InfoID.FILE_VIEW)
            addAll(getOverlayInfoIdList())
        }
    }

    fun getOverlayInfoIdList(): List<Int> {
        return ArrayList<Int>().apply {
            for (i in 0 until SolidCustomOverlayList.MAX_OVERLAYS) {
                add(InfoID.OVERLAY + i)
            }
        }
    }

    fun isEditable(iid: Int): Boolean {
        return iid == InfoID.FILE_VIEW || iid == InfoID.EDITOR_DRAFT ||
                (iid >= InfoID.OVERLAY && iid < InfoID.OVERLAY + SolidCustomOverlayList.MAX_OVERLAYS)
    }

}
