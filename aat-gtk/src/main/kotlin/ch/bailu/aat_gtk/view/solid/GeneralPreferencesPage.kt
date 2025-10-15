package ch.bailu.aat_gtk.view.solid

import ch.bailu.aat_gtk.solid.GtkSolidLocationProvider
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.preferences.general.SolidUnit
import ch.bailu.aat_lib.preferences.general.SolidWeight
import ch.bailu.aat_lib.preferences.location.SolidAdjustGpsAltitude
import ch.bailu.aat_lib.preferences.location.SolidAdjustGpsAltitudeValue
import ch.bailu.aat_lib.preferences.system.SolidStatusMessages
import ch.bailu.aat_lib.resources.Res
import ch.bailu.gtk.adw.PreferencesGroup
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.gtk.Window

class GeneralPreferencesPage(app: Application, window: Window, appContext: AppContext): PreferencesPageParent(Res.str().p_general(), "general") {

    init {
        page.add(PreferencesGroup().apply {
            setTitle(Res.str().p_general())
            add(SolidIndexComboRowView(SolidUnit(appContext.storage)).layout)
            add(SolidEntryView(SolidWeight(appContext.storage)).layout)
            add(SolidIndexComboRowView(SolidStatusMessages(appContext.storage)).layout)
            add(SolidFileSelectorView(appContext.dataDirectory , app, window).layout)
        })

        page.add(PreferencesGroup().apply {
            setTitle(Res.str().gps())
            add(SolidIndexComboRowView(GtkSolidLocationProvider(appContext.storage)).layout)
            add(SolidBooleanSwitchView(SolidAdjustGpsAltitude(appContext.storage)).layout)
            add(SolidEntryView(SolidAdjustGpsAltitudeValue(appContext.storage)).layout)
        })
    }
}
