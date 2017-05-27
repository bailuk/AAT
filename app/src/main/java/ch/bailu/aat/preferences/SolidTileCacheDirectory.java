package ch.bailu.aat.preferences;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;

import ch.bailu.aat.R;
import ch.bailu.aat.util.fs.AppDirectory;
import ch.bailu.aat.util.fs.AndroidVolumes;
import ch.bailu.simpleio.foc.Foc;


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
        final Foc f = new OldSolidTileCacheDirectory(getContext()).toFile();

        ArrayList<String> list = new ArrayList<>(5);

        add_w(list, f);

        if (list.size()==0)
            list = buildSelection(list);

        if (list.size()==0)
            list.add(f.toString());

        return list.get(0);
    }


    @Override
    public ArrayList<String> buildSelection(ArrayList<String> list) {
        AndroidVolumes volumes = new AndroidVolumes(getContext());

        for (Foc cache : volumes.getCaches()) {
            final Foc tiles = cache.child(AppDirectory.DIR_TILES);
            add_w(list, cache, tiles);
        }


        for (Foc vol : volumes.getVolumes()) {
            final Foc osmdroid = vol.child(AppDirectory.DIR_TILES_OSMDROID);
            final Foc aat = vol.child(AppDirectory.DIR_AAT_DATA + "/" + AppDirectory.DIR_TILES);

            add_w(list, osmdroid);
            add_w(list, aat);
        }


        for (Foc vol : volumes.getVolumes()) {
            final Foc osmdroid = vol.child(AppDirectory.DIR_TILES_OSMDROID);
            final Foc aat = vol.child(AppDirectory.DIR_AAT_DATA + "/" + AppDirectory.DIR_TILES);

            add_ro(list, osmdroid);
            add_ro(list, aat);
        }

        for (Foc vol : volumes.getVolumes()) {
            final Foc aat = vol.child(AppDirectory.DIR_AAT_DATA + "/" + AppDirectory.DIR_TILES);

            if (aat.isReachable()==false)
                add_ro(list, vol, aat);
        }

        return list;
    }



}
