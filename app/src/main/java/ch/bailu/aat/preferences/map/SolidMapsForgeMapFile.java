package ch.bailu.aat.preferences.map;

import android.content.Context;

import java.util.ArrayList;

import ch.bailu.aat.util.ToDo;
import ch.bailu.util_java.foc.Foc;

public class SolidMapsForgeMapFile extends SolidMapsForgeDirectory {
    public SolidMapsForgeMapFile(Context c) {
        super(c);
    }


    @Override
    public String getLabel() {
        return ToDo.translate("Offline map");
    }

    @Override
    public ArrayList<String> buildSelection(ArrayList<String> list) {
        final ArrayList<Foc> dirs = getWellKnownMapDirs();

        for (Foc dir : dirs) {
            add_ext(list, dir, EXTENSION);
            add_extInSubdirectories(list, dir, EXTENSION);
        }
        return list;
    }
}
