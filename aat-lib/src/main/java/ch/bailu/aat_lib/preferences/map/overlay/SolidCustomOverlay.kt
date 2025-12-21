package ch.bailu.aat_lib.preferences.map.overlay

import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.SolidOverlayFile
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.map.overlay.SolidOverlayFileEnabled
import ch.bailu.aat_lib.preferences.map.overlay.SolidOverlayInterface
import ch.bailu.foc.Foc
import ch.bailu.foc.FocFactory

/**
 * Custom Overlay
 * Can be enabled / disabled
 * Has custom file path
 * InfoID.Overlay...
 */
class SolidCustomOverlay(storage: StorageInterface, focFactory: FocFactory, private val iid: Int) :
    SolidOverlayInterface
{
    private val file = SolidOverlayFile(storage, focFactory, iid)
    private val enabled = SolidOverlayFileEnabled(storage, iid)

    fun setValueFromFile(file: Foc) {
        if (file.exists()) {
            this.file.setValue(file.path)
            enabled.value = true
        }
    }

    override fun getValueAsFile(): Foc {
        return file.getValueAsFile()
    }

    override fun getLabel(): String {
        return getValueAsFile().name
    }

    override fun getValueAsString(): String {
        return file.getValueAsString()
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
        return file.getKey()
    }

    override fun getStorage(): StorageInterface {
        return enabled.getStorage()
    }

    override fun hasKey(key: String): Boolean {
        return file.hasKey(key) || enabled.hasKey(key)
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
}