package ch.bailu.aat_gtk.view.solid

import ch.bailu.aat_gtk.config.Icons
import ch.bailu.gtk.adw.PreferencesPage

abstract class PreferencesPageParent(title: String, name: String) {
    val page = PreferencesPage().apply {
        setTitle(title)
        setName(name)
        iconName = Icons.preferencesOtherSymbolic
    }
}
