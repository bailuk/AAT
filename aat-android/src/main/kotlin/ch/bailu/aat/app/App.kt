package ch.bailu.aat.app

import android.app.Application
import ch.bailu.aat.broadcaster.AndroidBroadcaster
import ch.bailu.aat.util.AndroidLoggerFactory
import ch.bailu.aat_lib.app.AppConfig
import ch.bailu.aat_lib.app.AppGraphicFactory
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.logger.BroadcastLoggerFactory
import org.acra.config.dialog
import org.acra.config.mailSender
import org.acra.data.StringFormat
import org.acra.ktx.initAcra
import org.mapsforge.core.util.Parameters
import org.mapsforge.map.android.graphics.AndroidGraphicFactory

class App : Application() {
    override fun onCreate() {
        AppConfig.setInstance(AndroidAppConfig())
        initLogger()
        initMapsForge()
        if (AppConfig.getInstance().isRelease) {
            initAcraReport()
        }
        super.onCreate()
    }

    override fun onTerminate() {
        AppLog.set(AndroidLoggerFactory())
        super.onTerminate()
    }

    private fun initLogger() {
        AppLog.set(
            BroadcastLoggerFactory(
                AndroidBroadcaster(applicationContext),
                AndroidLoggerFactory()
            )
        )
    }

    private fun initMapsForge() {
        AndroidGraphicFactory.createInstance(this)
        AppGraphicFactory.set(AndroidGraphicFactory.INSTANCE)
        Parameters.SQUARE_FRAME_BUFFER = false
    }

    private fun initAcraReport() {
        initAcra {
            buildConfigClass = org.acra.BuildConfig::class.java
            reportFormat = StringFormat.KEY_VALUE_LIST
            dialog {
                title = "${AppConfig.getInstance().appName} crashed"
                text = """
                    This will open your e-mail app to send a crash report including some information about your device to "${AppConfig.getInstance().appContact}".
                    This will help the author to fix and improve this app.
                """.trimIndent()
            }
            mailSender {
                mailTo = AppConfig.getInstance().appContact
            }
        }
    }
}
