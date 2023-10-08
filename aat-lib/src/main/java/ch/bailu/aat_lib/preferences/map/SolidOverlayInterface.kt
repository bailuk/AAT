package ch.bailu.aat_lib.preferences.map

import ch.bailu.aat_lib.preferences.SolidFileInterface
import ch.bailu.aat_lib.preferences.SolidTypeInterface

interface SolidOverlayInterface : SolidTypeInterface, SolidFileInterface {
    fun isEnabled(): Boolean
    fun setEnabled(enabled: Boolean)
    fun getIID(): Int
}
