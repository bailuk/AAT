package ch.bailu.aat.preferences.map;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;

import ch.bailu.aat.R;
import ch.bailu.aat.preferences.SolidFile;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.util.fs.AndroidVolumes;
import ch.bailu.aat.util.fs.AppDirectory;
import ch.bailu.aat.util.fs.foc.FocAndroid;
import ch.bailu.util_java.foc.Foc;

public class SolidMapsForgeDirectory extends SolidFile {
    public final static String MAPS_DIR = "maps";
    public final static String ORUX_MAPS_DIR = "oruxmaps/mapfiles";

    private final static String KEY = SolidMapsForgeDirectory.class.getSimpleName();

    public SolidMapsForgeDirectory(Context c) {
        super(c, KEY);
    }

    @Override
    public String getLabel() {
        return getContext().getString(R.string.p_mapsforge_directory);
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


    public String getDefaultValue() {
        ArrayList<String> list = new ArrayList<>(5);

        list = buildSelection(list);

        File external = getContext().getExternalFilesDir(null);

        if (external != null) {
            add_w(list, FocAndroid.factory(getContext(), external.getAbsolutePath()));
        }

        if (list.size()>0) {
            return list.get(0);
        }
        return "";
    }


    @Override
    public ArrayList<String> buildSelection(ArrayList<String> list) {
        /*
        AndroidVolumes volumes = new AndroidVolumes(getContext());


        for (Foc f : volumes.getVolumes()) {
            final Foc maps1 = f.child(MAPS_DIR);
            final Foc maps2 = f.child(AppDirectory.DIR_AAT_DATA + "/" + MAPS_DIR);
            final Foc maps3 = f.child(ORUX_MAPS_DIR);

            add_r(list, maps1);
            add_subdirectories_r(list, maps1);

            add_r(list, maps2);
            add_subdirectories_r(list, maps2);

            add_r(list, maps3);
        }
        */


        final ArrayList<Foc> dirs = getWellKnownMapDirs();

        for (Foc dir : dirs) {
            add_r(list, dir);
            add_subdirectories_r(list, dir);
        }
        return list;
    }


    public ArrayList<Foc> getWellKnownMapDirs() {
        final ArrayList<Foc> dirs = new ArrayList<>(5);
        final AndroidVolumes volumes = new AndroidVolumes(getContext());

        for (Foc f : volumes.getVolumes()) {
            add_dr(dirs, f.child(MAPS_DIR));
            add_dr(dirs, f.child(AppDirectory.DIR_AAT_DATA + "/" + MAPS_DIR));
            add_dr(dirs, f.child(ORUX_MAPS_DIR));
        }

        return dirs;
    }

    private static void add_dr(ArrayList<Foc> dirs, Foc file) {
        if (file.canRead() && file.isDir()) {
            dirs.add(file);
        }
    }
}
