package ch.bailu.aat.preferences.presets;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.preferences.SolidAutopause;

public class SolidTrackerAutopause extends SolidAutopause {
    private final static String KEY="autopause";

    public SolidTrackerAutopause(Context c, int i) {
        super(c, KEY, i);
    }

    @Override
    public String getLabel() {
        return getContext().getString(R.string.p_tracker_autopause);
    }


}
