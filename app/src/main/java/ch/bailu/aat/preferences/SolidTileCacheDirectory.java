package ch.bailu.aat.preferences;

import java.util.ArrayList;

import ch.bailu.aat.R;
import android.content.Context;

public class SolidTileCacheDirectory extends SolidDirectoryList {
    private static final String KEY="TILE_CACHE_DIR";
    private static final String[] POSTFIX={"osmdroid/tiles", "aat_data/tiles"};

    public SolidTileCacheDirectory(Context c) {
        super(c, KEY);
    }


    @Override
    public String getLabel() {
        return getContext().getString(R.string.p_directory_tiles); 
    }


    @Override
    public void initList(ArrayList<String> list) {
        SolidDataDirectory.fillDirectoryList(list, POSTFIX);
        SolidDataDirectory.addFileToList(list, getContext().getCacheDir());
    }
}
