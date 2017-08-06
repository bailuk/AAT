package ch.bailu.aat.preferences;

import android.content.Context;

import ch.bailu.aat.R;

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
