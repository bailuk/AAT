package ch.bailu.aat.preferences;

import android.content.Context;

import java.util.ArrayList;

import ch.bailu.aat.R;

public class OldSolidDataDirectory extends OldSolidDirectoryList {
    private static final String KEY="TILE_DATA_DIR";
    private static final String[] POSTFIX={"aat_data"};

    public OldSolidDataDirectory(Context c) {
        super(c, KEY);
    }

    @Override
    public String getLabel() {
        return getContext().getString(R.string.p_directory_data);
    }
    
    
    @Override
    public void initList(ArrayList<String> list) {
        OldSolidDataDirectory.fillDirectoryList(list, POSTFIX);
        OldSolidDataDirectory.addFileToList(list, getContext().getFilesDir());
    }


}
