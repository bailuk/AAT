package ch.bailu.aat_lib.preferences

import ch.bailu.foc.FocFactory

class SolidOverlayFile(storageInterface: StorageInterface, focFactory: FocFactory, val infoID: Int):
    SolidFile(storageInterface, "SolidOverlayFile_$infoID", focFactory)
