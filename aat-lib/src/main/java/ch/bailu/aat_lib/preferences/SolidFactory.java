package ch.bailu.aat_lib.preferences;

import ch.bailu.aat_lib.preferences.location.SolidLocationProvider;
import ch.bailu.aat_lib.preferences.system.SolidDataDirectory;
import ch.bailu.foc.Foc;

public interface SolidFactory {

    StorageInterface getStorage();

    SolidLocationProvider getLocationProvider();
    SolidDataDirectory getDataDirectory();
}
