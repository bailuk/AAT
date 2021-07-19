package ch.bailu.aat.util.ui;

import android.app.Activity;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;

import java.io.Closeable;

import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.preferences.presets.SolidBacklight;
import ch.bailu.aat.services.InsideContext;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.StateID;
import ch.bailu.aat_lib.preferences.OnPreferencesChanged;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.preferences.presets.SolidPreset;

public class Backlight implements OnContentUpdatedInterface,
        OnPreferencesChanged, Closeable {

    private final ServiceContext scontext;
    private final Activity activity;
    private final Window window;

    private final SolidPreset spreset;
    private SolidBacklight sbacklight;

    private int state = StateID.OFF;


    public Backlight(Activity a, ServiceContext sc) {
        scontext = sc;
        activity = a;
        window = a.getWindow();

        spreset = new SolidPreset(new Storage(sc.getContext()));
        sbacklight = setToPreset();

        spreset.register(this);
    }


    public void setBacklightAndPreset() {
        setToPreset();
        setBacklight();
    }


    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        if (state != info.getState()) {
            state = info.getState();
            setBacklight();
        }
    }


    @Override
    public void onPreferencesChanged(StorageInterface s, String key) {
        if (spreset.hasKey(key)) {
            setBacklightAndPreset();

        } else if (sbacklight.hasKey(key)) {
            setBacklight();
        }
    }


    private SolidBacklight setToPreset() {
        int presetIndex = getPresetIndex();

        sbacklight = new SolidBacklight(scontext.getContext(), presetIndex);
        return sbacklight;
    }


    private int getPresetIndex() {
        final int[] result = new int[1];
        result[0] = 0;

        new InsideContext(scontext) {
            @Override
            public void run() {
                result[0] = scontext.getTrackerService().getPresetIndex();
            }
        };
        return result[0];
    }


    private void setBacklight() {
        if (state == StateID.ON && sbacklight.keepScreenOn()) {
            keepOn();
        } else {
            autoOff();
        }
    }


    private void autoOff() {
        keepScreenOn(false);
        dismissKeyGuard(false);
    }


    private void keepOn() {
        keepScreenOn(true);
        dismissKeyGuard(sbacklight.dismissKeyGuard());
    }


    private void dismissKeyGuard(boolean dismiss) {
        if (Build.VERSION.SDK_INT >= 27) {
            dismissKeyGuardSDK27(dismiss);
        } else if (Build.VERSION.SDK_INT >= 26) {
            dismissKeyGuardSDK26(dismiss);
        } else {
            dismissKeyGuardSDK5(dismiss);
        }
    }


    @RequiresApi(api = 27)
    private void dismissKeyGuardSDK27(boolean dismiss) {
        activity.setShowWhenLocked(dismiss);
    }


    @RequiresApi(api = 26)
    private void dismissKeyGuardSDK26(boolean dismiss) {
        updateFlags(dismiss, WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
    }


    private void dismissKeyGuardSDK5(boolean dismiss) {
        updateFlags(dismiss,
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                 | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

    }


    private void keepScreenOn(boolean keepOn) {
        updateFlags(keepOn, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void updateFlags(boolean on, int flags) {
        if (on) {
            window.addFlags(flags);
        } else {
            window.clearFlags(flags);
        }
    }

    @Override
    public void close() {
        spreset.unregister(this);
    }
}
