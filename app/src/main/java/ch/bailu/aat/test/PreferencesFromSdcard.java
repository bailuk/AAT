package ch.bailu.aat.test;

import ch.bailu.aat.preferences.Storage;
import android.content.Context;

public class PreferencesFromSdcard extends UnitTest {

    public PreferencesFromSdcard(Context c) {
        super(c);
    }

    @Override
    public void test() throws Exception {
        Storage storage = Storage.global(getContext());
        storage.restore();        
    }

}
