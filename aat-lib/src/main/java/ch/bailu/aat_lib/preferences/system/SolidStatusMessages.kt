package ch.bailu.aat_lib.preferences.system;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.preferences.SolidIndexList;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.resources.Res;

public class SolidStatusMessages extends SolidIndexList {

    private static final String KEY = "Status Messages";

    private final String[] VAL;

    public SolidStatusMessages(StorageInterface storage) {
        super(storage, KEY);
        VAL = new String[] {
                Res.str().p_messages_size(),
                Res.str().p_messages_url(),
                Res.str().p_messages_file(),
                Res.str().none()
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

    @Nonnull
    @Override
    public String getLabel() {
        return Res.str().p_messages();
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
