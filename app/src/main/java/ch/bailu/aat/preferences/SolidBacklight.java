package ch.bailu.aat.preferences;

import ch.bailu.aat.R;
import android.content.Context;
import android.os.PowerManager;

public class SolidBacklight extends SolidStaticIndexList {

    private final static String KEY="backlight";
    
    @SuppressWarnings("deprecation")
    private final static int LOCK_MODE[] = {
        PowerManager.PARTIAL_WAKE_LOCK, 
        PowerManager.SCREEN_DIM_WAKE_LOCK, 
        PowerManager.SCREEN_BRIGHT_WAKE_LOCK
        };

    public SolidBacklight(Context c, int i) {
        super(Storage.preset(c), KEY+i,
                new String[] {
                    c.getString(R.string.p_backlight_off),
                    c.getString(R.string.p_backlight_dim),
                    c.getString(R.string.p_backlight_bright)
                }
            );
    }
    
    public String getLabel() {
        return getContext().getString(R.string.p_backlight_title);
    }
    
    
    public int getLockMode() {
        return LOCK_MODE[getIndex()];
    }
}
