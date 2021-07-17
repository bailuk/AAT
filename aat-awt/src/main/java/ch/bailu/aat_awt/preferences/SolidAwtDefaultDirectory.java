package ch.bailu.aat_awt.preferences;

import java.util.ArrayList;

import ch.bailu.aat_lib.factory.FocFactory;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.preferences.system.SolidDataDirectoryDefault;

public class SolidAwtDefaultDirectory extends SolidDataDirectoryDefault {
    public SolidAwtDefaultDirectory(StorageInterface s, FocFactory focFactory) {
        super(s, focFactory);
    }

    @Override
    public ArrayList<String> buildSelection(ArrayList<String> list) {
        String home = System.getProperty("user.home");
        list.add(home + "/aat_data");

        return list;
    }
}
