package ch.bailu.aat.preferences.system;

import android.content.Context;

import ch.bailu.aat.preferences.SolidIndexList;
import ch.bailu.aat.util.ToDo;

public class SolidStatusMessages extends SolidIndexList {

    private static final String KEY = "Satus Messages";

    private static final String VAL[] = {ToDo.translate("Download size"),
            ToDo.translate("URL"),
            ToDo.translate("URL and filepath"),
            ToDo.translate("None")};

    public SolidStatusMessages(Context c) {
        super(c, KEY);
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
        return ToDo.translate(KEY);
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
