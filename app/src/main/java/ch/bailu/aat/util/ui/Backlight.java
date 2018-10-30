package ch.bailu.aat.util.ui;

import android.content.SharedPreferences;
import android.view.Window;
import android.view.WindowManager;

import java.io.Closeable;

import ch.bailu.aat.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.StateID;
import ch.bailu.aat.preferences.SolidBacklight;
import ch.bailu.aat.preferences.SolidPreset;
import ch.bailu.aat.services.InsideContext;
import ch.bailu.aat.services.ServiceContext;

public class Backlight implements OnContentUpdatedInterface,
        SharedPreferences.OnSharedPreferenceChangeListener, Closeable {

    private final ServiceContext scontext;
    private final Window window;


    private final SolidPreset spreset;
    private SolidBacklight sbacklight;

    private int state = StateID.OFF;

    public Backlight(Window w, ServiceContext sc) {
        scontext = sc;
        window = w;
        spreset = new SolidPreset(sc.getContext());
        sbacklight = setSolidBacklight();

        spreset.register(this);

    }




    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        state = info.getState();
        set();
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (spreset.hasKey(key)) {
            setSolidBacklight();
            set();

        } else if (sbacklight.hasKey(key)) {
            set();
        }
    }


    private SolidBacklight setSolidBacklight() {
        int presetIndex = getPresetIndex();
        sbacklight = new SolidBacklight(scontext.getContext(), presetIndex);
        return sbacklight;
    }


    private int getPresetIndex() {
        final int[] presetIndex = new int[1];
        new InsideContext(scontext) {
            @Override
            public void run() {
                presetIndex[0] = scontext.getTrackerService().getPresetIndex();
            }
        };
        return presetIndex[0];
    }


    private void set() {
        if (state == StateID.ON && sbacklight.keepScreenOn()) {
            keepOn();
        } else {
            autoOff();
        }
    }


    private void keepOn() {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }

    private void autoOff() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


    @Override
    public void close() {
        spreset.unregister(this);
    }
}
