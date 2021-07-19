package ch.bailu.aat_awt.app;

import ch.bailu.aat_lib.app.AppConfig;

public final class AwtAppConfig extends AppConfig {
    @Override
    public String getApplicationId() {
        return "ch.bailu.aat_awt";
    }

    @Override
    public String getVersionName() {
        return "v0.1 alpha";
    }

    @Override
    public int getVersionCode() {
        return 1;
    }

    @Override
    public boolean isRelease() {
        return false;
    }
}
