package ch.bailu.aat.views.description;

import android.content.Context;

import ch.bailu.aat.description.GpsStateDescription;
import ch.bailu.aat.util.ui.AppTheme;

public class GPSStateButton extends ColorNumberView {


    public GPSStateButton(Context c) {
        super(new GpsStateDescription(c), AppTheme.bar);

    }



    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        updateAllText();
    }
}
