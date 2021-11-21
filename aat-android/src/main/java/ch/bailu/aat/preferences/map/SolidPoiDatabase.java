package ch.bailu.aat.preferences.map;

import java.util.ArrayList;

import ch.bailu.aat_lib.factory.FocFactory;
import ch.bailu.aat_lib.preferences.SolidFile;
import ch.bailu.aat_lib.preferences.map.SolidMapsForgeDirectory;
import ch.bailu.aat_lib.resources.Res;
import ch.bailu.foc.Foc;

public class SolidPoiDatabase extends SolidFile {

    private final static String EXTENSION = ".poi";
    private final SolidMapsForgeDirectory smapDirectory;

    public SolidPoiDatabase(SolidMapsForgeDirectory smapDirectory, FocFactory focFactory) {
        super(smapDirectory.getStorage(), SolidPoiDatabase.class.getSimpleName(), focFactory);
        this.smapDirectory = smapDirectory;

    }

    @Override
    public String getLabel() {
        return Res.str().p_mapsforge_poi_db();
    }


    @Override
    public ArrayList<String> buildSelection(ArrayList<String> list) {

        ArrayList<Foc> dirs = smapDirectory.getWellKnownMapDirs();
        for (Foc dir: dirs) {
            add_ext(list, dir, EXTENSION);
            add_extInSubdirectories(list, dir, EXTENSION);
        }

        return list;
    }
}
