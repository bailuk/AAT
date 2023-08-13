package ch.bailu.aat_gtk.view.solid

import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.lib.extensions.margin
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.StateID
import ch.bailu.aat_lib.preferences.presets.SolidPreset

class SolidPresetComboView : OnContentUpdatedInterface {
    val layout = SolidIndexComboView(SolidPreset(GtkAppContext.storage)).apply {label.hide()}.layout.apply {margin(Layout.margin)}

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        layout.sensitive = info.state == StateID.OFF
    }
}
