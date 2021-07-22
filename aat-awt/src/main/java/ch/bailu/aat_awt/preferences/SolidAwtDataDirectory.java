package ch.bailu.aat_awt.preferences;

import ch.bailu.aat_lib.factory.FocFactory;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.preferences.system.SolidDataDirectory;

public class SolidAwtDataDirectory extends SolidDataDirectory {
    public SolidAwtDataDirectory(StorageInterface s, FocFactory focFactory) {
        super(new SolidAwtDefaultDirectory(s, focFactory), focFactory);
    }
}
