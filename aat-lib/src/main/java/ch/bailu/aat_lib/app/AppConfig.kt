package ch.bailu.aat_lib.app

import ch.bailu.aat_lib.Configuration
import ch.bailu.aat_lib.util.WithStatusText

abstract class AppConfig : WithStatusText {
    val appLongName: String
        get() = Configuration.appLongName
    val appName: String
        get() = Configuration.appName
    val appContact: String
        get() = Configuration.appContact
    open val appId: String
        get() = Configuration.appId
    open val appVersionName: String
        get() = Configuration.appVersionName
    open val appVersionCode: Int
        get() = 0
    val appWebsite: String
        get() = Configuration.appWebsite
    val appCopyright: String
        get() = Configuration.appCopyright
    abstract val isRelease: Boolean

    private val buildType: String
        get() = if (isRelease) {
            "Release"
        } else {
            "Debug"
        }

    val userAgent: String
        get() = appName + "/" +
                appLongName + "/" +
                appVersionName + " (" + appContact + ")"

    override fun appendStatusText(builder: StringBuilder) {
        builder.append("<p>")
            .append(appLongName)
            .append(" (")
            .append(appName)
            .append(")<br>")
            .append(appId)
            .append("<br>")
            .append(appVersionName)
            .append(" (")
            .append(appVersionCode)
            .append("), ")
            .append(buildType)
            .append("</p>")
    }


    companion object {
        private var instance: AppConfig? = null

        @JvmStatic
        fun getInstance(): AppConfig {
            val result = instance

            if (result == null) {
                throw RuntimeException("Instance is not set")
            } else {
                return result
            }

        }

        @JvmStatic
        fun setInstance(appConfig: AppConfig) {
            if (instance == null) {
                instance = appConfig
            } else {
                throw RuntimeException("Instance was already set")
            }
        }
    }
}
