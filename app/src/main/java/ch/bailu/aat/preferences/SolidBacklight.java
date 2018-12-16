package ch.bailu.aat.preferences;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.util.ToDo;

public class SolidBacklight extends SolidStaticIndexList {

    private final static String KEY="backlight";
    


    public SolidBacklight(Context c, int i) {
        super(c, KEY+i,
                new String[] {
                    c.getString(R.string.p_backlight_off),
                    c.getString(R.string.p_backlight_bright),
                    ToDo.translate("Keep on (disable screen lock)")
                }
            );
    }
    
    public String getLabel() {
        return getContext().getString(R.string.p_backlight_title);
    }

    public boolean keepScreenOn() {
        return getIndex() != 0;
    }

    public boolean dismissKeyGuard() {
        return (getIndex() == 2);
    }
}
