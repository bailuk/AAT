package ch.bailu.aat.util.ui;

import ch.bailu.aat.BuildConfig;
import ch.bailu.aat.util.WithStatusText;

public class AppConfig implements WithStatusText {
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


    @Override
    public void appendStatusText(StringBuilder builder) {
        builder .append("<p>")
                .append(getLongName())
                .append(" (")
                .append(getShortName())
                .append(")<br>")
                .append(BuildConfig.APPLICATION_ID)
                .append("<br>")
                .append(getVersionName())
                .append(" (")
                .append(BuildConfig.VERSION_CODE)
                .append("), ")
                .append(BuildConfig.BUILD_TYPE)
                .append("</p>");
    }
}
