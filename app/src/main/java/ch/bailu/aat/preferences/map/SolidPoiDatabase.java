package ch.bailu.aat.preferences.map;

import android.content.Context;

import java.util.ArrayList;

import ch.bailu.aat.R;
import ch.bailu.aat.preferences.SolidFile;
import ch.bailu.util_java.foc.Foc;

public class SolidPoiDatabase extends SolidFile {

    private final static String EXTENSION = ".poi";

    public SolidPoiDatabase(Context c) {
        super(c, SolidPoiDatabase.class.getSimpleName());
    }


    @Override
    public String getLabel() {
        return getString(R.string.p_mapsforge_poi_db);
    }


    @Override
    public ArrayList<String> buildSelection(ArrayList<String> list) {

        ArrayList<Foc> dirs = new SolidMapsForgeDirectory(getContext()).getWellKnownMapDirs();
        for (Foc dir: dirs) {
            add_ext(list, dir, EXTENSION);
            add_extInSubdirectories(list, dir, EXTENSION);
        }

        return list;
    }
}
