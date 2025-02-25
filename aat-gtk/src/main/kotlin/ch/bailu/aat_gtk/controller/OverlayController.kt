package ch.bailu.aat_gtk.controller

import ch.bailu.aat_lib.gpx.information.InformationUtil
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.map.SolidOverlayFileEnabled

class OverlayController(private val storage: StorageInterface, private val uiController: UiControllerInterface, private val iid: Int): OverlayControllerInterface {
    override fun setEnabled(enabled: Boolean) {
        uiController.setOverlayEnabled(iid, enabled)
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

    override fun edit() {
        uiController.loadIntoEditor(iid)
    }

    override fun isEditable(): Boolean {
        return InformationUtil.isEditable(iid)
    }

    companion object {
        fun createMapOverlayControllers(
            storage: StorageInterface,
            uiController: UiControllerInterface
        ): List<OverlayControllerInterface> {
            return createOverlayControllers(storage, uiController, InformationUtil.getMapOverlayInfoIdList())
        }

        fun createEditableOverlayControllers(
            storage: StorageInterface,
            uiController: UiControllerInterface
        ): List<OverlayControllerInterface> {
            return createOverlayControllers(storage, uiController, InformationUtil.getEditableOverlayInfoIdList())
        }

        private fun createOverlayControllers (
            storage: StorageInterface,
            uiController: UiControllerInterface,
            iidList: List<Int>
        ): List<OverlayControllerInterface> {
            return ArrayList<OverlayController>().apply {
                iidList.forEach { iid ->
                    add(OverlayController(storage, uiController, iid))
                }
            }
        }

    }
}
