package ch.bailu.aat_gtk.view.preferences

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.preferences.beacon.*
import ch.bailu.gtk.adw.PreferencesGroup
import ch.bailu.gtk.gtk.Application

class NetworkPreferencesPage(appContext: AppContext) : PreferencesPageParent("Network", "network") {
    init {
        page.add(PreferencesGroup().apply {
            setTitle("Beacon")
            add(SolidBooleanSwitchView(SolidBeaconEnabled(appContext.storage)).layout)
            add(SolidEntryView(SolidBeaconServer(appContext.storage)).layout)
            add(SolidEntryView(SolidBeaconKey(appContext.storage)).layout)
        })
    }
}
