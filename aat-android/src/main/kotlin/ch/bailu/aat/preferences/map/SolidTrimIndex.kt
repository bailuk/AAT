package ch.bailu.aat.preferences.map

import ch.bailu.aat_lib.preferences.SolidInteger
import ch.bailu.aat_lib.preferences.StorageInterface

/**
 * TODO move to lib
 */
class SolidTrimIndex(storageInterface: StorageInterface) : SolidInteger(storageInterface, SolidTrimIndex::class.java.simpleName)
