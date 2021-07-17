package ch.bailu.aat.preferences.presets;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat_lib.preferences.SolidStaticIndexList;
import ch.bailu.aat_lib.resources.Res;

public class SolidBacklight extends SolidStaticIndexList {

    private final static String KEY="backlight";



    public SolidBacklight(Context c, int i) {
        super(new Storage(c), KEY+i,
                new String[] {
                    c.getString(R.string.p_backlight_off),
                    c.getString(R.string.p_backlight_on),
                    c.getString(R.string.p_backlight_on_no_lock)
                }
            );
    }

    public String getLabel() {
        return Res.str().p_backlight_title();
    }

    public boolean keepScreenOn() {
        return getIndex() != 0;
    }

    public boolean dismissKeyGuard() {
        return (getIndex() == 2);
    }
}
