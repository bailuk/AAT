package ch.bailu.aat_lib.mock

import ch.bailu.aat_lib.app.AppConfig

object MockAppConfig {
    private var initialized = false

    fun init() {
        if (!initialized) {
            AppConfig.setInstance(object : AppConfig() {
                override val appId: String
                    get() = "ch.bailu.aat_test"

                override val appVersionName: String
                    get() = "v1.29"

                override val appVersionCode: Int
                    get() = 43

                override val isRelease: Boolean
                    get() = false
            })
            initialized = true
        }
    }}
