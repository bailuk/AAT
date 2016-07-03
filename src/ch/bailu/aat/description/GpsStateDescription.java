package ch.bailu.aat.description;

import android.content.Context;
import ch.bailu.aat.R;
import ch.bailu.aat.preferences.SolidGPSLock;
import ch.bailu.aat.preferences.Storage;

public class GpsStateDescription extends StateDescription {

    private final SolidGPSLock slock;
    public GpsStateDescription(Context c) {
        super(c);
        slock=new SolidGPSLock(Storage.global(c));
    }

    @Override
    public String getLabel() {
        return getString(R.string.gps);
    }

    
    @Override
    public String getUnit() {
        return slock.getString();
    }
}
