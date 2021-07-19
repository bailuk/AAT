package ch.bailu.aat_lib.app;

import ch.bailu.aat_lib.util.WithStatusText;

public abstract class AppConfig implements WithStatusText {
    public String getLongName() {
        return "AAT Activity Tracker";
    }
    public String getShortName() {
        return "AAT";
    }
    public String getContact() {
        return "aat@bailu.ch";
    }

    public abstract String getApplicationId();
    public abstract String getVersionName();
    public abstract int getVersionCode();

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
