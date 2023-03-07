package ch.bailu.aat.app;

import ch.bailu.aat.BuildConfig;
import ch.bailu.aat_lib.app.AppConfig;

public final class AndroidAppConfig extends AppConfig {
    @Override
    public boolean isRelease() {
        return !BuildConfig.DEBUG;
    }
}
