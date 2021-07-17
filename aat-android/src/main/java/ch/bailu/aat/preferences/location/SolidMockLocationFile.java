package ch.bailu.aat.preferences.location;

import android.content.Context;

import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat_lib.preferences.SolidString;

public class SolidMockLocationFile extends SolidString {
    private final static String KEY="mock_file";


    public SolidMockLocationFile(Context c) {
        super(new Storage(c), KEY);
    }

}
