package ch.bailu.aat;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.testng.annotations.BeforeClass;

import ch.bailu.aat.app.AndroidAppConfig;
import ch.bailu.aat.util.AndroidLogger;
import ch.bailu.aat_lib.app.AppConfig;
import ch.bailu.aat_lib.logger.AppLog;

public class AppLogTest {

    public static String logged = "";
    private static boolean initialized = false;

    @BeforeClass
    public static void init() {
        if (!initialized) {
            AppConfig.setInstance(new AndroidAppConfig());
            AppLog.set(new AndroidLogger());
            initialized = true;
        }
    }


    @Test
    public void testDebug() {
        logged = "";
        AppLog.d(null, null);


        if (AppConfig.getInstance().isRelease()) {
            assertEquals("" , logged);
        } else {
            assertEquals("DEBUG: AppLog: ", logged);
        }
    }

    @Test
    public void testWarning() {
        AppLog.w(this, new Throwable("message"));
        assertEquals("WARN: AppLogTest: [Throwable] message", logged);

        AppLog.w(this, "message");
        assertEquals("WARN: AppLogTest: message", logged);

        AppLog.w(this, (String)null);
        assertEquals("WARN: AppLogTest: ", logged);

        AppLog.w(this, (Throwable) null);
        assertEquals("WARN: AppLogTest: ", logged);
    }
}
