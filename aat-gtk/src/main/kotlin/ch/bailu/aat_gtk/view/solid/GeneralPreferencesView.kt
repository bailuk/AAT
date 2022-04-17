package ch.bailu.aat_gtk.view.solid

import ch.bailu.aat_gtk.app.GtkAppConfig
import ch.bailu.aat_gtk.solid.GtkSolidLocationProvider
import ch.bailu.aat_gtk.solid.SolidGtkDataDirectory
import ch.bailu.aat_gtk.view.VerticalScrollView
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.general.SolidPresetCount
import ch.bailu.aat_lib.preferences.general.SolidUnit
import ch.bailu.aat_lib.preferences.general.SolidWeight
import ch.bailu.aat_lib.preferences.location.SolidAdjustGpsAltitude
import ch.bailu.aat_lib.preferences.location.SolidAdjustGpsAltitudeValue
import ch.bailu.aat_lib.preferences.system.SolidCacheSize
import ch.bailu.aat_lib.preferences.system.SolidStatusMessages
import ch.bailu.aat_lib.resources.Res
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.gtk.Window
import ch.bailu.gtk.helper.ActionHelper

class GeneralPreferencesView(storage: StorageInterface, app: Application, window: Window) : VerticalScrollView() {
    init {
        add(Res.str().p_general())
        add(SolidIndexComboView(SolidUnit(storage)).layout)
        add(SolidEntryView(SolidWeight(storage)).layout)
        add(SolidIndexComboView(SolidPresetCount(storage)).layout)
        add(SolidIndexComboView(SolidStatusMessages(storage)).layout)

        add(Res.str().gps())
        add(SolidIndexComboView(GtkSolidLocationProvider(storage)).layout)
        add(SolidBooleanSwitchView(SolidAdjustGpsAltitude(storage)).layout)
        add(SolidEntryView(SolidAdjustGpsAltitudeValue(storage)).layout)

        add(Res.str().files())
        add(SolidDirectorySelectorView(SolidGtkDataDirectory(storage, GtkAppConfig.Foc) , app, window).layout)
        add(SolidIndexComboView(SolidCacheSize(storage)).layout)
    }
}
