package ch.bailu.aat.views.description;

import android.content.Context;

import ch.bailu.aat.description.GpsStateDescription;

public class GPSStateButton extends NumberButton {


    public GPSStateButton(Context c) {
        super(new GpsStateDescription(c));
    }



    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        updateAllText();
    }
}
