package ch.bailu.aat.preferences;

import android.content.Context;
import android.os.PowerManager;
import android.view.WindowManager;

import ch.bailu.aat.R;

public class SolidBacklight extends SolidStaticIndexList {

    private final static String KEY="backlight";
    


    public SolidBacklight(Context c, int i) {
        super(c, KEY+i,
                new String[] {
                    c.getString(R.string.p_backlight_off),
                    c.getString(R.string.p_backlight_bright)
                }
            );
    }
    
    public String getLabel() {
        return getContext().getString(R.string.p_backlight_title);
    }
    
    
    public boolean keepScreenOn() {
        return getIndex() != 0;
    }
}
