package ch.bailu.aat.preferences.system;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.preferences.SolidIndexList;

public class SolidStatusMessages extends SolidIndexList {

    private static final String KEY = "Status Messages";

    private final String[] VAL;

    public SolidStatusMessages(Context c) {
        super(c, KEY);
        VAL = new String[] {
                getString(R.string.p_messages_size),
                getString(R.string.p_messages_url),
                getString(R.string.p_messages_file),
                getString(R.string.none)
        };
    }

    @Override
    public int length() {
        return VAL.length;
    }

    @Override
    protected String getValueAsString(int i) {
        return VAL[i];
    }

    @Override
    public String getLabel() {
        return getString(R.string.p_messages);
    }


    public boolean showPath() {
        return getIndex()==2;
    }
    public boolean showURL() {
        return getIndex()==1 || getIndex()==2;
    }
    public boolean showSummary() {
        return getIndex()==0;
    }
}
