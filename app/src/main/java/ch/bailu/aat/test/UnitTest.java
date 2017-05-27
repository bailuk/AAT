package ch.bailu.aat.test;

import android.content.Context;

import junit.framework.Assert;

import java.io.File;

import ch.bailu.aat.util.fs.AppDirectory;
import ch.bailu.aat.util.ContextWrapperInterface;
import ch.bailu.simpleio.foc.Foc;

public abstract class UnitTest extends Assert implements ContextWrapperInterface {
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
