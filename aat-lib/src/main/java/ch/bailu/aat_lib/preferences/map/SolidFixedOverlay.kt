package ch.bailu.aat_lib.preferences.map

import ch.bailu.aat_lib.gpx.information.InformationUtil
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.system.SolidDataDirectory
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.util.fs.AppDirectory
import ch.bailu.foc.Foc

/**
 * Base class for overly files with fixed file path
 *  example: POI and Draft
 */
abstract class SolidFixedOverlay(
    private val baseDirectory: SolidDataDirectory,
    private val iid: Int,
    private val subDir: String,
    private val fileName: String
) : SolidOverlayInterface {
    private val enabled: SolidOverlayFileEnabled =
        SolidOverlayFileEnabled(baseDirectory.getStorage(), iid)

    override fun getValueAsFile(): Foc {
        return directory.child(fileName)
    }

    override fun getValueAsString(): String {
        return getValueAsFile().toString()
    }

    val directory: Foc
        get() = AppDirectory.getDataDirectory(baseDirectory, subDir)

    override fun getKey(): String {
        return enabled.getKey()
    }

    override fun getStorage(): StorageInterface {
        return enabled.getStorage()
    }

    override fun hasKey(key: String): Boolean {
        return enabled.hasKey(key) || baseDirectory.hasKey(key)
    }

    override fun register(listener: OnPreferencesChanged) {
        enabled.register(listener)
    }

    override fun unregister(listener: OnPreferencesChanged) {
        enabled.unregister(listener)
    }

    override fun isEnabled(): Boolean {
        return enabled.isEnabled
    }

    override fun setEnabled(enabled: Boolean) {
        this.enabled.value = enabled
    }

    override fun getToolTip(): String? {
        return null
    }

    override fun getIID(): Int {
        return iid
    }

    override fun getLabel(): String {
        return InformationUtil.defaultName(iid)
    }
}
