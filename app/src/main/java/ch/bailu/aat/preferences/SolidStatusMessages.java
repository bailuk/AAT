package ch.bailu.aat.preferences;

import android.content.Context;

public class SolidStatusMessages extends SolidIndexList {

    private static final String KEY = "SatusMessages";

    public SolidStatusMessages(Context c) {
        super(c, KEY);
    }

    @Override
    public int length() {
        return 0;
    }

    @Override
    protected String getValueAsString(int i) {
        return null;
    }
}
