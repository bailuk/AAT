package ch.bailu.aat_gtk.solid

import ch.bailu.aat_lib.factory.FocFactory
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.system.SolidDataDirectory

class SolidGtkDataDirectory (s: StorageInterface, focFactory: FocFactory) :
    SolidDataDirectory(SolidGtkDefaultDirectory(s, focFactory), focFactory)
