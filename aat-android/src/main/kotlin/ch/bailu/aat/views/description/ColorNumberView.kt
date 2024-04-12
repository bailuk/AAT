package ch.bailu.aat.views.description

import android.content.Context
import ch.bailu.aat.util.ui.theme.UiTheme
import ch.bailu.aat_lib.description.ContentDescription
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.StateID


open class ColorNumberView(context: Context, c: ContentDescription, theme: UiTheme) : NumberView(context, c, theme) {
    private var state = StateID.OFF

    override fun onContentUpdated(iid: Int,  info: GpxInformation) {
        super.onContentUpdated(iid, info)
        if (state != info.getState()) {
            state = info.getState()
            if (state == StateID.ON) {
                setHighlightUnitLabelColor()
            } else {
                setDefaultUnitLabelColor()
            }
        }
    }
}
