package ch.bailu.aat.test;

import android.content.Context;

import ch.bailu.aat.preferences.Storage;

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
