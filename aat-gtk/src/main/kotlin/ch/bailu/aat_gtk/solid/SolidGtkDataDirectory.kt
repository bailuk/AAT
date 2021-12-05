package ch.bailu.aat_gtk.solid

import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.system.SolidDataDirectory
import ch.bailu.foc.FocFactory
import ch.bailu.foc.FocFile

class SolidGtkDataDirectory (s: StorageInterface, focFactory: FocFactory) :
    SolidDataDirectory(SolidGtkDefaultDirectory(s, focFactory), { string: String? -> FocFile(string) }) {
}
