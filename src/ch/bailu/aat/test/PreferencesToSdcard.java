package ch.bailu.aat.test;

import ch.bailu.aat.preferences.Storage;
import android.content.Context;

public class PreferencesToSdcard extends UnitTest {

    public PreferencesToSdcard(Context c) {
        super(c);
        
    }

    @Override
    public void test() throws Exception {
        Storage storage = Storage.global(getContext());
        
        storage.backup();
    }

}
