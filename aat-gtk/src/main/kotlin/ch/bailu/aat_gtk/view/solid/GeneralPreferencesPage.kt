package ch.bailu.aat_gtk.view.solid

import ch.bailu.aat_gtk.solid.GtkSolidLocationProvider
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.preferences.general.SolidPresetCount
import ch.bailu.aat_lib.preferences.general.SolidUnit
import ch.bailu.aat_lib.preferences.general.SolidWeight
import ch.bailu.aat_lib.preferences.location.SolidAdjustGpsAltitude
import ch.bailu.aat_lib.preferences.location.SolidAdjustGpsAltitudeValue
import ch.bailu.aat_lib.preferences.system.SolidCacheSize
import ch.bailu.aat_lib.preferences.system.SolidStatusMessages
import ch.bailu.aat_lib.resources.Res
import ch.bailu.gtk.adw.Window
import ch.bailu.gtk.gtk.Application

class GeneralPreferencesPage(app: Application, window: Window, appContext: AppContext): PreferencesPageParent(Res.str().p_general(), "general") {

    init {
        add(Res.str().p_general())
        add(SolidIndexComboView(SolidUnit(appContext.storage)).layout)
        add(SolidEntryView(SolidWeight(appContext.storage)).layout)
        add(SolidIndexComboView(SolidPresetCount(appContext.storage)).layout)
        add(SolidIndexComboView(SolidStatusMessages(appContext.storage)).layout)

        add(Res.str().gps())
        add(SolidIndexComboView(GtkSolidLocationProvider(appContext.storage)).layout)
        add(SolidBooleanSwitchView(SolidAdjustGpsAltitude(appContext.storage)).layout)
        add(SolidEntryView(SolidAdjustGpsAltitudeValue(appContext.storage)).layout)

        add(Res.str().files())
        add(SolidDirectorySelectorView(appContext.dataDirectory , app, window).layout)
        add(SolidIndexComboView(SolidCacheSize(appContext.storage)).layout)
    }
}
