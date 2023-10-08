package ch.bailu.aat_lib.preferences.location;

import ch.bailu.aat_lib.preferences.SolidString;
import ch.bailu.aat_lib.preferences.StorageInterface;

public class SolidMockLocationFile extends SolidString {
    private final static String KEY="mock_file";


    public SolidMockLocationFile(StorageInterface storage) {
        super(storage, KEY);
    }

}
