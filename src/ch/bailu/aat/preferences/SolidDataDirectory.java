package ch.bailu.aat.preferences;

import java.util.ArrayList;

import ch.bailu.aat.R;
import android.content.Context;

public class SolidDataDirectory extends SolidDirectoryList {
    private static final String KEY="TILE_DATA_DIR";
    private static final String[] POSTFIX={"aat_data"};

    public SolidDataDirectory(Context c) {
        super(c, KEY);
    }

    @Override
    public String getLabel() {
        return getContext().getString(R.string.p_directory_data);
    }
    
    
    @Override
    public void initList(ArrayList<String> list) {
        SolidDataDirectory.fillDirectoryList(list, POSTFIX);
        SolidDataDirectory.addFileToList(list, getContext().getFilesDir());
    }
}
