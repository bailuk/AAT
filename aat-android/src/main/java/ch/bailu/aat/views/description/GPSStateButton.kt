package ch.bailu.aat.views.description

import android.content.Context
import ch.bailu.aat.util.ui.theme.AppTheme
import ch.bailu.aat_lib.description.GpsStateDescription

class GPSStateButton(context: Context) : ColorNumberView(context, GpsStateDescription(), AppTheme.bar) {
    public override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        updateAllText()
    }
}
