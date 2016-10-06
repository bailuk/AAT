package ch.bailu.aat.preferences;

import android.content.Context;
import android.os.PowerManager;
import android.view.WindowManager;

import ch.bailu.aat.R;

public class SolidBacklight extends SolidStaticIndexList {

    private final static String KEY="backlight";
    
    @SuppressWarnings("deprecation")
    private final static int LOCK_MODE[] = {
        PowerManager.PARTIAL_WAKE_LOCK, 
        PowerManager.SCREEN_DIM_WAKE_LOCK,
        PowerManager.SCREEN_BRIGHT_WAKE_LOCK
        };

    /**
     * TODO change from PowerManager to Window flags
     * This is the now official way of doing things
     *  in Activity:
     *  getWindow().addFlags(LOCK_FLAGS);
     *
     */
    private final static int LOCK_FLAGS =
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
            WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
            WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
            WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON;

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
