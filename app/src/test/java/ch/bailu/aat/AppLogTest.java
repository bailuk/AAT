package ch.bailu.aat;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.bailu.aat.util.ui.AppLog;

public class AppLogTest {

    public static String logged = "";

    @Test
    public void testDebug() {
        AppLog.d(null, null);
        assertEquals("DEBUG: : ", logged);
    }

    @Test
    public void testWarning() {
        AppLog.w(this, new Throwable("message"));
        assertEquals("WARN: AppLogTest: message", logged);

        AppLog.w(this, "message");
        assertEquals("WARN: AppLogTest: message", logged);

        AppLog.w(this, (String)null);
        assertEquals("WARN: AppLogTest: ", logged);

        AppLog.w(this, (Throwable) null);
        assertEquals("WARN: AppLogTest: ", logged);
    }
}
