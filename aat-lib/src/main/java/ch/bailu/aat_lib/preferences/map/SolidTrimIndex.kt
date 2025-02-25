package ch.bailu.aat_lib.preferences.map

import ch.bailu.aat_lib.preferences.SolidInteger
import ch.bailu.aat_lib.preferences.StorageInterface

class SolidTrimIndex(storageInterface: StorageInterface) : SolidInteger(storageInterface, SolidTrimIndex::class.java.simpleName)
