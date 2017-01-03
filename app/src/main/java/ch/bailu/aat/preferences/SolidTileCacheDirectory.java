package ch.bailu.aat.preferences;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;

import ch.bailu.aat.R;
import ch.bailu.aat.util.fs.AppDirectory;


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
            r = getDefaultValue();
            setValue(r);
        }
        return r;
    }


    private String getDefaultValue() {
        final File f = new OldSolidTileCacheDirectory(getContext()).toFile();

        ArrayList<String> list = new ArrayList<>(5);

        add(list, f);

        if (list.size()==0)
            list = buildSelection(list);

        if (list.size()==0)
            list.add(f.getAbsolutePath());

        return list.get(0);
    }


    @Override
    public ArrayList<String> buildSelection(ArrayList<String> list) {

        final File external_cache = getContext().getExternalCacheDir();
        final File external_tiles = new File(external_cache, AppDirectory.DIR_TILES);

        add(list, external_cache, external_tiles);


        final File sdcard = Environment.getExternalStorageDirectory();
        final File sdcard_osmdroid = new File(sdcard, AppDirectory.DIR_TILES_OSMDROID);
        final File sdcard_aat = new File(sdcard,
                AppDirectory.DIR_AAT_DATA+ "/" + AppDirectory.DIR_TILES);

        add(list, sdcard_osmdroid);
        add(list, sdcard, sdcard_aat);


        final File internal_cache = getContext().getCacheDir();
        final File internal_tiles = new File(internal_cache, AppDirectory.DIR_TILES);

        add(list, internal_cache, internal_tiles);


        return list;
    }



}
