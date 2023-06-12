package ch.bailu.aat.preferences.map;

import android.content.Context;

import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat_lib.preferences.SolidInteger;

public class SolidTrimIndex extends SolidInteger {
    public SolidTrimIndex(Context c) {
        super(new Storage(c), SolidTrimIndex.class.getSimpleName());
    }
}
