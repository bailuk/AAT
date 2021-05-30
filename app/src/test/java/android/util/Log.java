package android.util;

import ch.bailu.aat.AppLogTest;

/**
 * mock log functions
 */
public class Log {

    public static int d(String tag, String msg) {
        AppLogTest.logged = "DEBUG: " + tag + ": " + msg;
        System.out.println(AppLogTest.logged);
        return 0;
    }

    public static int i(String tag, String msg) {
        AppLogTest.logged = "INFO: " + tag + ": " + msg;
        System.out.println(AppLogTest.logged);
        return 0;
    }

    public static int w(String tag, String msg) {
        AppLogTest.logged = "WARN: " + tag + ": " + msg;
        System.out.println(AppLogTest.logged);
        return 0;
    }

    public static int e(String tag, String msg) {
        AppLogTest.logged = "ERROR: " + tag + ": " + msg;
        System.out.println(AppLogTest.logged);
        return 0;
    }

}