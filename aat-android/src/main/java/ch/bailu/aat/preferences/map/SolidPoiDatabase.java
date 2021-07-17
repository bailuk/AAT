package ch.bailu.aat.preferences.map;

import android.content.Context;

import java.util.ArrayList;

import ch.bailu.aat.factory.AndroidFocFactory;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat_lib.preferences.SolidFile;
import ch.bailu.aat_lib.resources.Res;
import ch.bailu.foc.Foc;

public class SolidPoiDatabase extends SolidFile {

    private final static String EXTENSION = ".poi";

    public SolidPoiDatabase(Context c) {
        super(new Storage(c), SolidPoiDatabase.class.getSimpleName(), new AndroidFocFactory(c));
        context = c;
    }

    private final Context context;


    public Context getContext() {
        return context;
    }
    @Override
    public String getLabel() {
        return Res.str().p_mapsforge_poi_db();
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
