package ch.bailu.aat_lib.preferences.map;

import java.util.ArrayList;

import ch.bailu.aat_lib.factory.FocFactory;
import ch.bailu.aat_lib.preferences.SolidFile;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.resources.Res;
import ch.bailu.foc.Foc;

public abstract class SolidMapsForgeDirectory extends SolidFile {

    public final static String EXTENSION = ".map";
    public final static String MAPS_DIR = "maps";
    public final static String ORUX_MAPS_DIR = "oruxmaps/mapfiles";

    private final static String KEY = SolidMapsForgeDirectory.class.getSimpleName();

    public SolidMapsForgeDirectory(StorageInterface storageInterface, FocFactory factory) {
        super(storageInterface, KEY, factory);
    }


    @Override
    public String getLabel() {
        return Res.str().p_mapsforge_directory();
    }




    @Override
    public String getValueAsString() {
        String r = super.getValueAsString();


        if (getStorage().isDefaultString(r)) {
            r = getDefaultValue();

            setValue(r);
        }
        return r;
    }



    public abstract String getDefaultValue();


    @Override
    public ArrayList<String> buildSelection(ArrayList<String> list) {
        final ArrayList<Foc> dirs = getWellKnownMapDirs();

        for (Foc dir : dirs) {
            add_r(list, dir);
            add_subdirectories_r(list, dir);
        }
        return list;
    }


    public abstract ArrayList<Foc> getWellKnownMapDirs();

    protected static void add_dr(ArrayList<Foc> dirs, Foc file) {
        if (file.canRead() && file.isDir()) {
            dirs.add(file);
        }
    }
}
