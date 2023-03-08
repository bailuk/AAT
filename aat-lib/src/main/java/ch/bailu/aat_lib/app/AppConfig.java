package ch.bailu.aat_lib.app;

import ch.bailu.aat_lib.Configuration;
import ch.bailu.aat_lib.util.WithStatusText;

public abstract class AppConfig implements WithStatusText {
    public String getLongName() {
        return Configuration.appLongName;
    }
    public String getShortName() {
        return Configuration.appName;
    }
    public String getContact() {
        return Configuration.appContact;
    }

    public String getApplicationId() {
        return Configuration.appId;
    }

    public String getVersionName() {
        return Configuration.appVersionName;
    }

    public int getVersionCode() {
        return Integer.parseInt(Configuration.appVersionCode);
    }

    public String getWebsite() {
        return Configuration.appWebsite;
    }

    public String getCopyright() {
        return Configuration.appCopyright;
    }

    public abstract boolean isRelease();

    private static AppConfig instance = null;

    public static void setInstance(AppConfig instance) {
        if (AppConfig.instance == null) {
            AppConfig.instance = instance;
        } else {
            throw new RuntimeException("Instance was already set");
        }
    }

    public static AppConfig getInstance() {
        if (AppConfig.instance == null) {
            throw new RuntimeException("Instance is not set");
        }
        return AppConfig.instance;
    }

    public String getBuildType() {
        if (isRelease()) {
            return "Release";
        } else {
            return "Debug";
        }
    }

    public String getUserAgent() {
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
                .append(getApplicationId())
                .append("<br>")
                .append(getVersionName())
                .append(" (")
                .append(getVersionCode())
                .append("), ")
                .append(getBuildType())
                .append("</p>");
    }
}
