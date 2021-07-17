package ch.bailu.aat.preferences;

import android.content.Context;

import ch.bailu.aat.preferences.location.AndroidSolidLocationProvider;
import ch.bailu.aat.preferences.system.AndroidSolidDataDirectory;
import ch.bailu.aat_lib.preferences.SolidFactory;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.preferences.location.SolidLocationProvider;
import ch.bailu.aat_lib.preferences.system.SolidDataDirectory;

public class AndroidSolidFactory implements SolidFactory {

    private final Context context;

    public AndroidSolidFactory(Context context) {
        this.context = context;
    }

    @Override
    public StorageInterface getStorage() {
        return new Storage(context);
    }

    @Override
    public SolidLocationProvider getLocationProvider() {
        return new AndroidSolidLocationProvider(context);
    }

    @Override
    public SolidDataDirectory getDataDirectory() {
        return new AndroidSolidDataDirectory(context);
    }
}
