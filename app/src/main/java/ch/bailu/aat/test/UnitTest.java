package ch.bailu.aat.test;

import android.content.Context;



import ch.bailu.aat.util.fs.AppDirectory;
import ch.bailu.aat.util.ContextWrapperInterface;
import ch.bailu.util_java.foc.Foc;

public abstract class UnitTest extends FakeAssert implements ContextWrapperInterface {
    private final Context context;


    public UnitTest(Context c) {
        context=c;
    }


    public abstract void test() throws Exception;


    @Override
    public Context getContext() {
        return context;
    }


    public static Foc getTestDirectory(Context context) {
        return AppDirectory.getDataDirectory(context, AppDirectory.DIR_TEST);
    }



}
