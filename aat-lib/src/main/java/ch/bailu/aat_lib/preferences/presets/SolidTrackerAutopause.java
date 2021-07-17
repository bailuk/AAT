package ch.bailu.aat_lib.preferences.presets;

import ch.bailu.aat_lib.preferences.SolidAutopause;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.resources.Res;

public class SolidTrackerAutopause extends SolidAutopause {
    private final static String KEY="autopause";

    public SolidTrackerAutopause(StorageInterface s, int i) {
        super(s, KEY, i);
    }

    @Override
    public String getLabel() {
        return Res.str().p_tracker_autopause();
    }


}
