package ch.bailu.aat.views.description;

import android.content.Context;

import ch.bailu.aat.util.ui.theme.AppTheme;
import ch.bailu.aat_lib.description.GpsStateDescription;

public class GPSStateButton extends ColorNumberView {


    public GPSStateButton(Context c) {
        super(c,new GpsStateDescription(), AppTheme.bar);

    }



    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        updateAllText();
    }
}
