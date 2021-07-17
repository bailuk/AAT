package ch.bailu.aat.factory;

import android.content.Context;

import ch.bailu.aat_lib.factory.FocFactory;
import ch.bailu.foc.Foc;
import ch.bailu.foc_android.FocAndroid;

public class AndroidFocFactory implements FocFactory {

    private final Context context;


    public AndroidFocFactory(Context c) {
        context = c;
    }


    @Override
    public Foc toFoc(String string) {
        return FocAndroid.factory(context, string);
    }
}
