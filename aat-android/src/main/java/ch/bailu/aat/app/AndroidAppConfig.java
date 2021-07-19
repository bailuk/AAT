package ch.bailu.aat.app;

import ch.bailu.aat.BuildConfig;
import ch.bailu.aat_lib.app.AppConfig;

public final class AndroidAppConfig extends AppConfig {

    @Override
    public String getVersionName() {
        return BuildConfig.VERSION_NAME;
    }

    @Override
    public int getVersionCode() {
        return BuildConfig.VERSION_CODE;
    }

    @Override
    public boolean isRelease() {
        return !BuildConfig.DEBUG;
    }


    @Override
    public String getApplicationId() {
        return "ch.bailu.aat";
    }
}
