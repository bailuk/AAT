package ch.bailu.aat.preferences.system;

import android.content.Context;

import ch.bailu.aat_lib.preferences.system.SolidDataDirectory;
import ch.bailu.foc_android.FocAndroidFactory;

public class AndroidSolidDataDirectory extends SolidDataDirectory {

    public AndroidSolidDataDirectory(Context c) {
        super(new AndroidSolidDataDirectoryDefault(c), new FocAndroidFactory(c));
    }
}

