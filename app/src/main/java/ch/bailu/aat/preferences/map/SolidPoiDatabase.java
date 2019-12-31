package ch.bailu.aat.preferences.map;

import android.content.Context;

import java.util.ArrayList;

import ch.bailu.aat.preferences.SolidFile;
import ch.bailu.aat.util.ToDo;
import ch.bailu.util_java.foc.Foc;

public class SolidPoiDatabase extends SolidFile {

    private final static String EXTENSION = ".poi";

    public SolidPoiDatabase(Context c) {
        super(c, SolidPoiDatabase.class.getSimpleName());
    }


    @Override
    public String getLabel() {
        return ToDo.translate("MapsForge POI Database");
    }


    @Override
    public ArrayList<String> buildSelection(ArrayList<String> list) {

        Foc maps = new SolidMapsForgeDirectory(getContext()).getValueAsFile();
        add_ext(list, maps, EXTENSION);
        add_extInSubdirectories(list, maps, EXTENSION);


        ArrayList<Foc> dirs = new SolidMapsForgeDirectory(getContext()).getWellKnownMapDirs();
        for (Foc dir: dirs) {
            add_ext(list, maps, EXTENSION);
            add_extInSubdirectories(list, dir, EXTENSION);
        }

        return list;
    }
}
