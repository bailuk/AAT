package ch.bailu.aat_awt.preferences;

import ch.bailu.aat_lib.factory.FocFactory;
import ch.bailu.aat_lib.preferences.SolidFactory;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.preferences.location.SolidLocationProvider;
import ch.bailu.aat_lib.preferences.system.SolidDataDirectory;
import ch.bailu.aat_lib.preferences.system.SolidDataDirectoryDefault;
import ch.bailu.foc.FocFile;

public class AwtSolidFactory implements SolidFactory {
    private final StorageInterface storage = new AwtStorage();
    private final FocFactory focFactory = string -> new FocFile(string);


    @Override
    public StorageInterface getStorage() {
        return storage;
    }

    @Override
    public SolidLocationProvider getLocationProvider() {
        return new SolidGeoClue2Provider(storage);
    }

    @Override
    public SolidDataDirectory getDataDirectory() {
        SolidDataDirectoryDefault sdefault = new SolidAwtDefaultDirectory(storage, focFactory);
        return new SolidAwtDataDirectory(storage, sdefault, focFactory);
    }
}
