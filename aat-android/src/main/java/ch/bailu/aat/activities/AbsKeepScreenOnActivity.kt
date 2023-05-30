package ch.bailu.aat.activities;

import android.os.Bundle;

import ch.bailu.aat.util.ui.Backlight;
import ch.bailu.aat_lib.gpx.InfoID;

public abstract class AbsKeepScreenOnActivity extends ActivityContext {

    private Backlight backlight;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        backlight = new Backlight(this, getServiceContext());

        addTarget(backlight, InfoID.TRACKER);
    }


    @Override
    public void onResumeWithService() {
        super.onResumeWithService();

        backlight.setBacklightAndPreset();
    }


    @Override
    public void onDestroy() {
        backlight.close();
        super.onDestroy();
    }

}
