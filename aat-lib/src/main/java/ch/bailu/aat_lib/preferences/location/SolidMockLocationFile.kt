package ch.bailu.aat_lib.preferences.location

import ch.bailu.aat_lib.preferences.SolidString
import ch.bailu.aat_lib.preferences.StorageInterface

class SolidMockLocationFile(storage: StorageInterface) : SolidString(storage, KEY) {
    companion object {
        private const val KEY = "mock_file"
    }
}
