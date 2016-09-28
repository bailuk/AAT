package ch.bailu.aat.preferences;

import android.content.Context;

public class SolidTrimIndex extends SolidInteger {
    public SolidTrimIndex(Context context) {
        super(Storage.global(context), SolidTrimIndex.class.getSimpleName());
    }
}
