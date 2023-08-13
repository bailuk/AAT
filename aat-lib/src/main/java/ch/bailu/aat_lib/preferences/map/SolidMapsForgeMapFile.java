package ch.bailu.aat_lib.preferences.map;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.resources.Res;
import ch.bailu.foc.Foc;
import ch.bailu.foc.FocFactory;

public class SolidMapsForgeMapFile extends SolidMapsForgeDirectory {

    private final MapDirectories directories;

    public SolidMapsForgeMapFile(StorageInterface storage, FocFactory factory, MapDirectories directories) {
        super(storage, factory, directories);
        this.directories = directories;
    }


    @Nonnull
    @Override
    public String getLabel() {
        return Res.str().p_offline_map();
    }

    @Override
    public ArrayList<String> buildSelection(ArrayList<String> list) {
        final ArrayList<Foc> dirs = directories.getWellKnownMapDirs();

        for (Foc dir : dirs) {
            add_ext(list, dir, EXTENSION);
            add_extInSubdirectories(list, dir, EXTENSION);
        }
        return list;
    }
}
