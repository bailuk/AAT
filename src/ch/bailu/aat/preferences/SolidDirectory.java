package ch.bailu.aat.preferences;

import android.content.Context;

public class SolidDirectory extends SolidString {
    private static final String KEY_DIR_DIRECTORY="DIR_DIRECTORY";
    private static final String KEY_DIR_INDEX="DIR_INDEX_";

    
    public SolidDirectory(Context c) {
        super(Storage.global(c), KEY_DIR_DIRECTORY);

    }

    
    public void setPosition(int p) {
        new SolidInteger(getStorage(), KEY_DIR_INDEX+getValue()).setValue(p);
    }
    
    public int getPosition() {
        return new SolidInteger(getStorage(), KEY_DIR_INDEX+getValue()).getValue();
    }
    
    public String getSelection() {
        return "";
    }
}
