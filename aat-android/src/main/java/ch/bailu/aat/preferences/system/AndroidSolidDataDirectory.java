package ch.bailu.aat.preferences.system;

import android.content.Context;

import ch.bailu.aat.factory.AndroidFocFactory;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat_lib.preferences.system.SolidDataDirectory;

public class AndroidSolidDataDirectory extends SolidDataDirectory {


    public AndroidSolidDataDirectory(Context c) {
        super(new Storage(c), new AndroidSolidDataDirectoryDefault(c), new AndroidFocFactory(c));
    }
}

