package ch.bailu.aat_gtk.ui.view

import ch.bailu.aat_gtk.app.GtkAppConfig
import ch.bailu.aat_gtk.solid.GtkSolidLocationProvider
import ch.bailu.aat_gtk.solid.SolidGtkDataDirectory
import ch.bailu.aat_gtk.solid.SolidGtkDefaultDirectory
import ch.bailu.aat_gtk.ui.view.solid.SolidBooleanSwitchView
import ch.bailu.aat_gtk.ui.view.solid.SolidDirectorySelectorView
import ch.bailu.aat_gtk.ui.view.solid.SolidIndexComboView
import ch.bailu.aat_gtk.ui.view.solid.SolidEntryView
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.general.SolidPresetCount
import ch.bailu.aat_lib.preferences.general.SolidUnit
import ch.bailu.aat_lib.preferences.general.SolidWeight
import ch.bailu.aat_lib.preferences.location.SolidAdjustGpsAltitude
import ch.bailu.aat_lib.preferences.system.SolidDataDirectory
import ch.bailu.aat_lib.preferences.system.SolidStatusMessages
import ch.bailu.aat_lib.resources.Res
import ch.bailu.foc.FocFile
import ch.bailu.gtk.GTK
import ch.bailu.gtk.gtk.*
import ch.bailu.gtk.gtk.Label
import ch.bailu.gtk.type.Str

class PreferencesView(storage: StorageInterface, window: Window) {
    var container = Box(Orientation.VERTICAL,5)


    init {
        container.borderWidth = 5
        add(Res.str().p_general())
        add(SolidIndexComboView(SolidUnit(storage)).layout)
        add(SolidEntryView(SolidWeight(storage)).layout)
        add(SolidIndexComboView(SolidPresetCount(storage)).layout)
        add(SolidIndexComboView(SolidStatusMessages(storage)).layout)

        add(Res.str().gps())
        add(SolidIndexComboView(GtkSolidLocationProvider(storage)).layout)
        add(SolidBooleanSwitchView(SolidAdjustGpsAltitude(storage)).layout)
        add(SolidEntryView(SolidAdjustGpsAltitude(storage)).layout)

        add(Res.str().files())
        add(SolidDirectorySelectorView(SolidGtkDataDirectory(storage, GtkAppConfig.FFactory) , window).layout)
        add(SolidIndexComboView((storage)).layout)
    }

    private fun add(child: Widget) {
        container.packStart(child, GTK.FALSE, GTK.TRUE, 5)
    }

    private fun add(text: String) {
        val label = Label(null)
        label.setMarkup(Str("<b>${text}</b>"))
        add(label)
    }
}