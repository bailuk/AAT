package ch.bailu.aat_lib.preferences.map;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.preferences.SelectionList;
import ch.bailu.aat_lib.preferences.SolidFile;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.resources.Res;
import ch.bailu.foc.Foc;
import ch.bailu.foc.FocFactory;

public class SolidMapsForgeDirectory extends SolidFile {

    public final static String EXTENSION = ".map";
    public final static String MAPS_DIR = "maps";
    public final static String ORUX_MAPS_DIR = "oruxmaps/mapfiles";

    private final static String KEY = SolidMapsForgeDirectory.class.getSimpleName();

    private final MapDirectories directories;

    public SolidMapsForgeDirectory(StorageInterface storage, FocFactory factory, MapDirectories directories) {
        super(storage, KEY, factory);
        this.directories = directories;
    }

    @Nonnull
    @Override
    public String getLabel() {
        return Res.str().p_mapsforge_directory();
    }

    @Nonnull
    @Override
    public String getValueAsString() {
        String r = super.getValueAsString();


        if (getStorage().isDefaultString(r)) {
            r = getDefaultValue();

            setValue(r);
        }
        return r;
    }

    public String getDefaultValue() {
        ArrayList<String> list = new ArrayList<>(5);

        list = buildSelection(list);

        Foc external = directories.getDefault();
        if (external != null) {
            SelectionList.add_w(list, external);
        }

        if (list.size()>0) {
            return list.get(0);
        }
        return "";
    }

    @Override
    public ArrayList<String> buildSelection(ArrayList<String> list) {
        final ArrayList<Foc> dirs = directories.getWellKnownMapDirs();

        for (Foc dir : dirs) {
            SelectionList.add_r(list, dir);
            SelectionList.add_subdirectories_r(list, dir);
        }
        return list;
    }

    public ArrayList<Foc> getWellKnownMapDirs() {
        return directories.getWellKnownMapDirs();
    }
}
