package ch.bailu.aat_lib.preferences.map

import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.preferences.SolidCheckList
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.resources.Res
import ch.bailu.foc.FocFactory
import javax.annotation.Nonnull

class SolidCustomOverlayList(storage: StorageInterface, focFactory: FocFactory) :
    SolidCheckList() {
    private val list = ArrayList<SolidCustomOverlay>(MAX_OVERLAYS)

    init {
        for (i in 0 until MAX_OVERLAYS) {
            list.add(SolidCustomOverlay(storage, focFactory, InfoID.OVERLAY + i))
        }
    }

    operator fun get(index: Int): SolidCustomOverlay {
        var i = index
        i = Math.min(MAX_OVERLAYS - 1, i)
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
        val array = BooleanArray(MAX_OVERLAYS)
        for (i in list.indices) array[i] = list[i].isEnabled()
        return array
    }

    override fun setEnabled(i: Int, isChecked: Boolean) {
        get(i).setEnabled(isChecked)
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

    @Nonnull
    override fun getLabel(): String {
        return Res.str().file_overlay()
    }

    override fun getIconResource(): String {
        return "view_paged_inverse"
    }

    companion object {
        const val MAX_OVERLAYS = 4
    }
}
