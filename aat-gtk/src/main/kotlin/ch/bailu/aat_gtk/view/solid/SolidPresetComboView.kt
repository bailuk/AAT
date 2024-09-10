package ch.bailu.aat_gtk.view.solid

import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.util.extensions.margin
import ch.bailu.aat_lib.dispatcher.TargetInterface
import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.gpx.information.StateID
import ch.bailu.aat_lib.preferences.presets.SolidPreset

class SolidPresetComboView : TargetInterface {
    val layout = SolidIndexComboView(SolidPreset(GtkAppContext.storage)).apply {label.hide()}.layout.apply {margin(Layout.margin)}

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        layout.sensitive = info.getState() == StateID.OFF
    }
}
