package ch.bailu.aat_awt.preferences;

import ch.bailu.aat_lib.factory.FocFactory;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.preferences.system.SolidDataDirectory;
import ch.bailu.aat_lib.preferences.system.SolidDataDirectoryDefault;
import ch.bailu.foc.FocFile;

public class SolidAwtDataDirectory extends SolidDataDirectory {
    public SolidAwtDataDirectory(StorageInterface s, SolidDataDirectoryDefault defaultDirectory, FocFactory focFactory) {
        super(s, defaultDirectory, focFactory);
    }
}
