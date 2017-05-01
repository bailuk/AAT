package ch.bailu.aat.preferences;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;

import ch.bailu.aat.R;
import ch.bailu.aat.util.fs.AppDirectory;
import ch.bailu.aat.util.fs.AndroidVolumes;


public class SolidTileCacheDirectory extends SolidFile {

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

        add_w(list, f);

        if (list.size()==0)
            list = buildSelection(list);

        if (list.size()==0)
            list.add(f.getAbsolutePath());

        return list.get(0);
    }


    @Override
    public ArrayList<String> buildSelection(ArrayList<String> list) {
        AndroidVolumes volumes = new AndroidVolumes(getContext());

        for (File cache : volumes.getCaches()) {
            final File tiles = new File(cache, AppDirectory.DIR_TILES);
            add_w(list, cache, tiles);
        }


        for (File vol : volumes.getVolumes()) {
            final File osmdroid = new File(vol, AppDirectory.DIR_TILES_OSMDROID);
            final File aat = new File(vol,
                    AppDirectory.DIR_AAT_DATA + "/" + AppDirectory.DIR_TILES);

            add_w(list, osmdroid);
            add_w(list, aat);
        }


        for (File vol : volumes.getVolumes()) {
            final File osmdroid = new File(vol, AppDirectory.DIR_TILES_OSMDROID);
            final File aat = new File(vol,
                    AppDirectory.DIR_AAT_DATA + "/" + AppDirectory.DIR_TILES);

            add_ro(list, osmdroid);
            add_ro(list, aat);
        }

        for (File vol : volumes.getVolumes()) {
            final File aat = new File(vol,
                    AppDirectory.DIR_AAT_DATA + "/" + AppDirectory.DIR_TILES);

            if (aat.exists()==false)
                add_ro(list, vol, aat);
        }

        return list;
    }



}
