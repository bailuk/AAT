package ch.bailu.aat_lib.mock

import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.system.SolidDataDirectory
import ch.bailu.foc.FocFactory

class MockSolidDataDirectory(storageInterface: StorageInterface, focFactory: FocFactory):
    SolidDataDirectory(storageInterface, focFactory)
