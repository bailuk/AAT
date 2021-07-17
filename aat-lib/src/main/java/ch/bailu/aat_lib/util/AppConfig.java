package ch.bailu.aat_lib.util;

/**
 * TODO replace with something abstract that must be implemented by library consumers
 * This is a dummy for global app configuration
 */
public class AppConfig implements WithStatusText {
    static public String getLongName() {
        return "AAT";
    }
    static public String getShortName() {
        return "AAT";
    }
    static public String getVersionName() {
        return "v 11";
    }
    static public String getContact() {
        return "me@there";
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
                .append(APPLICATION_ID)
                .append("<br>")
                .append(getVersionName())
                .append(" (")
                .append(VERSION_CODE)
                .append("), ")
                .append(BUILD_TYPE)
                .append("</p>");
    }
    public static final String APPLICATION_ID = "ch.bailu.aat";
    public static final String VERSION_CODE = "10X 3000";
    public static final String BUILD_TYPE = "Alpha Station";

}
