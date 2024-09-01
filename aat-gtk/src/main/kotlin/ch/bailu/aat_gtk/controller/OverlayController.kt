package ch.bailu.aat_gtk.controller

import ch.bailu.aat_lib.gpx.information.InformationUtil
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.map.SolidOverlayFileEnabled

class OverlayController(private val storage: StorageInterface, private val uiController: UiControllerInterface, private val iid: Int): OverlayControllerInterface {
    override fun setEnabled(enabled: Boolean) {
        SolidOverlayFileEnabled(storage, iid).value = enabled
    }

    override fun frame() {
        uiController.showMap()
        uiController.frameInMap(iid)
    }

    override fun center() {
        uiController.showMap()
        uiController.centerInMap(iid)
    }

    override fun getName(): String {
        return uiController.getName(iid)
    }

    override fun isEnabled(): Boolean {
        return SolidOverlayFileEnabled(storage, iid).value
    }

    override fun showInDetail() {
        uiController.showDetail()
        uiController.showInDetail(iid)
    }

    companion object {
        fun createMapOverlayControllers(
            storage: StorageInterface,
            uiController: UiControllerInterface
        ): List<OverlayControllerInterface> {
            return ArrayList<OverlayController>().apply {
                InformationUtil.getMapOverlayInfoIdList().forEach { iid ->
                    add(OverlayController(storage, uiController, iid))
                }
            }
        }
    }
}
