package ch.bailu.aat

import ch.bailu.aat.app.AndroidAppConfig
import ch.bailu.aat.util.AndroidLoggerFactory
import ch.bailu.aat_lib.app.AppConfig
import ch.bailu.aat_lib.logger.AppLog
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.testng.annotations.BeforeClass

class AppLogTest {
    @Test
    fun testDebug() {
        logged = ""
        AppLog.d(null, null)
        if (AppConfig.getInstance().isRelease) {
            Assertions.assertEquals("", logged)
        } else {
            Assertions.assertEquals("DEBUG: AppLog: ", logged)
        }
    }

    @Test
    fun testWarning() {
        AppLog.w(this, Throwable("message"))
        Assertions.assertEquals("WARN: AppLogTest: [Throwable] message", logged)
        AppLog.w(this, "message")
        Assertions.assertEquals("WARN: AppLogTest: message", logged)
        AppLog.w(this, null as String?)
        Assertions.assertEquals("WARN: AppLogTest: ", logged)
        AppLog.w(this, null as Throwable?)
        Assertions.assertEquals("WARN: AppLogTest: ", logged)
    }

    companion object {
        var logged = ""
        private var initialized = false
        @BeforeClass
        fun init() {
            if (!initialized) {
                AppConfig.setInstance(AndroidAppConfig())
                AppLog.set(AndroidLoggerFactory())
                initialized = true
            }
        }
    }
}
