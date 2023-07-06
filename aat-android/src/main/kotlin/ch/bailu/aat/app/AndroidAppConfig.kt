package ch.bailu.aat.app

import ch.bailu.aat.BuildConfig
import ch.bailu.aat_lib.app.AppConfig

class AndroidAppConfig : AppConfig() {
    override fun isRelease(): Boolean {
        return !BuildConfig.DEBUG
    }
}
