package ch.bailu.aat.preferences;

import android.content.Context;

public class SolidMockLocationFile extends SolidString {
    private final static String KEY="mock_file";

    /*
    public SolidMockLocationFile(Storage s) {
        super(s, KEY);
    }
*/
    
    public SolidMockLocationFile(Context c) {
        super(Storage.global(c), KEY);
    }

}
