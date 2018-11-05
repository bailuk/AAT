package ch.bailu.aat.activities;

import android.os.Bundle;

import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.util.ui.Backlight;

public abstract class AbsKeepScreenOnActivity extends ActivityContext {

    private Backlight backlight;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        backlight = new Backlight(getWindow(), getServiceContext());

        addTargets(backlight, InfoID.TRACKER);
    }

    @Override
    public void onDestroy() {
        backlight.close();
        super.onDestroy();
    }

}
