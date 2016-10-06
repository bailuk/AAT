package ch.bailu.aat.preferences;

import android.content.Context;

import java.util.ArrayList;

import ch.bailu.aat.R;

public class OldSolidTileCacheDirectory extends OldSolidDirectoryList {
    private static final String KEY="TILE_CACHE_DIR";
    private static final String[] POSTFIX={"osmdroid/tiles", "aat_data/tiles"};

    public OldSolidTileCacheDirectory(Context c) {
        super(c, KEY);
    }


    @Override
    public String getLabel() {
        return getContext().getString(R.string.p_directory_tiles); 
    }


    @Override
    public void initList(ArrayList<String> list) {
        OldSolidDataDirectory.fillDirectoryList(list, POSTFIX);
        OldSolidDataDirectory.addFileToList(list, getContext().getCacheDir());
    }
}
