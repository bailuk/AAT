package ch.bailu.aat_lib.preferences.map;

import java.util.ArrayList;

import ch.bailu.foc.Foc;

/**
 * Abstract factory class for map specific
 * configuration
 */
public interface MapDirectories {

    ArrayList<Foc> getWellKnownMapDirs();

    Foc getDefault();

    SolidMapsForgeDirectory createSolidDirectory();
    SolidMapsForgeMapFile createSolidFile();
    SolidRenderTheme createSolidRenderTheme();
}
