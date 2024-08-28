package ch.bailu.aat_lib.preferences.map

import ch.bailu.aat_lib.preferences.SolidBoolean
import ch.bailu.aat_lib.preferences.StorageInterface

class SolidOverlayFileEnabled(storage: StorageInterface, val infoID: Int) : SolidBoolean(
    storage, "overlay_enabled_$infoID"
)
