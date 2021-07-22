package ch.bailu.aat.preferences.system;

import android.content.Context;

import ch.bailu.aat.factory.AndroidFocFactory;
import ch.bailu.aat_lib.preferences.system.SolidDataDirectory;

public class AndroidSolidDataDirectory extends SolidDataDirectory {

    public AndroidSolidDataDirectory(Context c) {
        super(new AndroidSolidDataDirectoryDefault(c), new AndroidFocFactory(c));
    }
}

