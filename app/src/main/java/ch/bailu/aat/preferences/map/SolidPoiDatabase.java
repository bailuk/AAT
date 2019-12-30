package ch.bailu.aat.preferences.map;

import android.content.Context;

import java.util.ArrayList;

import ch.bailu.aat.preferences.SolidFile;
import ch.bailu.aat.util.ToDo;
import ch.bailu.aat.util.ui.AppDensity;
import ch.bailu.util_java.foc.Foc;
import ch.bailu.util_java.util.Objects;

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

/*
    public void setToValid() {
        ArrayList<String> list = new ArrayList<>(5);

        list = buildSelection(list);

        String current = getValueAsString();

        for (String e : list) {
            if (Objects.equals(e, current)) return;
        }

        if (list.size() > 0) setValue(list.get(0));
    }
 */


}
