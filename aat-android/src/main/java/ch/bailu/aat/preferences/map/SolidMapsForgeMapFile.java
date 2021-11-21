package ch.bailu.aat.preferences.map;

import android.content.Context;

import java.util.ArrayList;

import ch.bailu.aat_lib.resources.Res;
import ch.bailu.foc.Foc;

public class SolidMapsForgeMapFile extends AndroidSolidMapsForgeDirectory {
    public SolidMapsForgeMapFile(Context c) {
        super(c);
    }


    @Override
    public String getLabel() {
        return Res.str().p_offline_map();
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
