package ch.bailu.aat_lib.preferences.map.overlay

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.preferences.SolidCheckList
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.map.overlay.SolidCustomOverlay.Companion.MAX_OVERLAYS
import ch.bailu.aat_lib.resources.Res

class SolidOverlayList<T: SolidOverlayInterface>(private val list: ArrayList<T>): SolidCheckList() {

    companion object {
        fun createMapOverlayList(appContext: AppContext): SolidOverlayList<SolidOverlayInterface> {
            val list = ArrayList<SolidOverlayInterface>()

            for (i in 0 until MAX_OVERLAYS) {
                list.add(SolidCustomOverlay(appContext.storage, appContext, InfoID.OVERLAY + i))
            }
            list.add(SolidPoiOverlay(appContext.dataDirectory))
            list.add(SolidOverpassOverlay(appContext.dataDirectory))
            list.add(SolidNominatimOverlay(appContext.dataDirectory))
            list.add(SolidBrouterOverlay(appContext.dataDirectory))
            list.add(SolidNominatimReverseOverlay(appContext.dataDirectory))
            return SolidOverlayList(list)
        }

        fun createCustomOverlayList(appContext: AppContext): SolidOverlayList<SolidCustomOverlay> {
            val list = ArrayList<SolidCustomOverlay>()

            for (i in 0 until MAX_OVERLAYS) {
                list.add(SolidCustomOverlay(appContext.storage, appContext, InfoID.OVERLAY + i))
            }
            return SolidOverlayList(list)
        }
    }


    operator fun get(index: Int): T {
        var i = index
        i = Math.min(list.size - 1, i)
        i = Math.max(0, i)
        return list[i]
    }

    override fun getStringArray(): Array<String> {
        val result = ArrayList<String>()

        list.forEach {
            result.add(it.getLabel())
        }
        return result.toTypedArray()
    }

    override fun getEnabledArray(): BooleanArray {
        val array = BooleanArray(list.size)
        for (i in list.indices) array[i] = list[i].isEnabled()
        return array
    }

    override fun setEnabled(index: Int, isChecked: Boolean) {
        get(index).setEnabled(isChecked)
    }

    override fun getKey(): String {
        return list[0].getKey()
    }

    override fun getStorage(): StorageInterface {
        return list[0].getStorage()
    }

    override fun hasKey(key: String): Boolean {
        for (aList in list) if (aList.hasKey(key)) return true
        return false
    }

    override fun getLabel(): String {
        return Res.str().p_overlay()
    }

    override fun getIconResource(): String {
        return "view_paged_inverse"
    }
}
