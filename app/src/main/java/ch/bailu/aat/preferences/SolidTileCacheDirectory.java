package ch.bailu.aat.preferences;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;

import ch.bailu.aat.R;
import ch.bailu.aat.helpers.AppDirectory;


public class SolidTileCacheDirectory extends SolidDirectory {

    public SolidTileCacheDirectory(Context c) {
        super(Storage.global(c), SolidTileCacheDirectory.class.getSimpleName());
    }


    @Override
    public String getLabel() {
        return getContext().getString(R.string.p_directory_tiles);
    }


    @Override
    public String getValueAsString() {
        String r = super.getValueAsString();

        if (r.equals(Storage.DEF_VALUE)) {
            r = new OldSolidTileCacheDirectory(getContext()).getValueAsString();
        }
        return r;
    }


    @Override
    public ArrayList<String> buildSelection(ArrayList<String> list) {
        File aat_data = new SolidDataDirectory(getContext()).getValueAsFile();
        File internal = getContext().getCacheDir();
        File external = Environment.getExternalStorageDirectory();
        File extcache = getContext().getExternalCacheDir();


        list.add(internal.getAbsolutePath() + "/" + AppDirectory.DIR_TILES);
        list.add(extcache.getAbsolutePath() + "/" + AppDirectory.DIR_TILES);
        list.add(external.getAbsolutePath() + "/" + AppDirectory.DIR_TILES_OSMDROID);

        if (aat_data.getAbsolutePath().endsWith("/aat_data"))
            list.add(aat_data.getAbsolutePath() + "/" + AppDirectory.DIR_TILES);

        return list;
    }
}
