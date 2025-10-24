package ch.bailu.aat_lib.gpx.information

import ch.bailu.aat_lib.preferences.map.overlay.SolidCustomOverlayList
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.resources.ToDo

object InformationUtil {

    fun defaultName(iid: Int): String {
        // TODO: use enum for this
        when(iid) {
            InfoID.POI -> return Res.str().p_mapsforge_poi()
            InfoID.EDITOR_DRAFT -> return ToDo.translate("Draft")
            InfoID.TRACKER -> return Res.str().tracker()
            InfoID.NOMINATIM -> return "Nominatim"
            InfoID.OVERPASS -> return Res.str().query_overpass()
            InfoID.EDITOR_OVERLAY -> return ToDo.translate("Editor")
            InfoID.FILE_VIEW -> return ToDo.translate("Selected File")
            InfoID.LIST_SUMMARY -> return ToDo.translate("List Summary")
            InfoID.CRITICAL_MAP -> return "Critical Map"
            InfoID.NOMINATIM_REVERSE -> return "Reverse"
            InfoID.BROUTER -> return "Brouter"
        }
        if (isOverlay(iid)) {
            return "${ToDo.translate("Overlay")} ${getOverlayIndex(iid)}"
        }
        return ""
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
