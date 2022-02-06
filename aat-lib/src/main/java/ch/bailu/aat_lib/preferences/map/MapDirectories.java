package ch.bailu.aat_lib.preferences.map;

import java.util.ArrayList;

import ch.bailu.foc.Foc;

public interface MapDirectories {

    ArrayList<Foc> getWellKnownMapDirs();

    Foc getDefault();
}
