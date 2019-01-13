package ch.bailu.aat.util.ui;

import ch.bailu.aat.BuildConfig;

public class AppString {
    static public String getLongName() {
        return BuildConfig.APP_NAME;
    }
    static public String getShortName() {
        return BuildConfig.APP_SNAME;
    }
    static public String getVersionName() {
        return BuildConfig.VERSION_NAME;
    }
    static public String getContact() {
        return BuildConfig.APP_CONTACT;
    }

    public static String getUserAgent() {
        return getShortName() + "/" +
               getLongName() + "/" +
               getVersionName() + " (" + getContact() + ")";

    }
}
