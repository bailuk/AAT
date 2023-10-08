package ch.bailu.aat_lib.preferences.map

import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.logger.AppLog.e
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.SolidString
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.foc.Foc
import ch.bailu.foc.FocFactory

/**
 * Custom Overlay
 * Can be enabled / disabled
 * Has custom file path
 * InfoID.Overlay...
 */
class SolidCustomOverlay(storage: StorageInterface, focFactory: FocFactory, iid: Int) :
    SolidOverlayInterface {
    private val path: SolidString
    private val enabled: SolidOverlayFileEnabled
    private val focFactory: FocFactory
    private val iid: Int

    init {
        validateOverlayIID(iid)
        this.iid = iid
        path = SolidString(storage, KEY_NAME + iid)
        enabled = SolidOverlayFileEnabled(storage, iid)
        this.focFactory = focFactory
    }

    private fun validateOverlayIID(i: Int) {
        if (!(i >= InfoID.OVERLAY && i < InfoID.OVERLAY + SolidCustomOverlayList.MAX_OVERLAYS)) {
            e(this, "Invalid overlay ID: $i")
        }
    }

    fun setValueFromFile(file: Foc) {
        if (file.exists()) {
            path.setValue(file.path)
            enabled.value = true
        }
    }

    override fun getValueAsFile(): Foc {
        return focFactory.toFoc(getValueAsString())
    }

    override fun getLabel(): String {
        return getValueAsFile().name
    }

    override fun getValueAsString(): String {
        return path.getValueAsString()
    }

    override fun isEnabled(): Boolean {
        return getValueAsFile().exists() && enabled.value
    }

    override fun setEnabled(enabled: Boolean) {
        this.enabled.value = enabled
    }

    override fun getIID(): Int {
        return iid
    }

    override fun getKey(): String {
        return path.getKey()
    }

    override fun getStorage(): StorageInterface {
        return enabled.getStorage()
    }

    override fun hasKey(key: String): Boolean {
        return key.contains(KEY_NAME) || enabled.hasKey(key)
    }

    override fun register(listener: OnPreferencesChanged) {
        getStorage().register(listener)
    }

    override fun unregister(listener: OnPreferencesChanged) {
        getStorage().unregister(listener)
    }

    override fun getToolTip(): String? {
        return null
    }

    companion object {
        private const val KEY_NAME = "overlay_path_"
    }
}
