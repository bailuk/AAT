package ch.bailu.aat.app

import ch.bailu.aat.BuildConfig
import ch.bailu.aat_lib.app.AppConfig

class AndroidAppConfig : AppConfig() {
    override val isRelease = !BuildConfig.DEBUG

    // Version Code can not be taken from from variable (f-droid version checker fail)
    override val appVersionCode = BuildConfig.VERSION_CODE

    override val appId: String
        get() = BuildConfig.APPLICATION_ID
}
