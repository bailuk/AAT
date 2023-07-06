package ch.bailu.aat.app

import android.app.Application
import ch.bailu.aat.BuildConfig
import ch.bailu.aat.dispatcher.AndroidBroadcaster
import ch.bailu.aat.util.AndroidLoggerFactory
import ch.bailu.aat_lib.app.AppConfig
import ch.bailu.aat_lib.app.AppGraphicFactory
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.logger.BroadcastLoggerFactory
import org.acra.ACRA
import org.acra.config.CoreConfigurationBuilder
import org.acra.config.DialogConfigurationBuilder
import org.acra.config.MailSenderConfigurationBuilder
import org.acra.data.StringFormat
import org.mapsforge.core.util.Parameters
import org.mapsforge.map.android.graphics.AndroidGraphicFactory

class App : Application() {
    override fun onCreate() {
        AppConfig.setInstance(AndroidAppConfig())
        initLogger()
        initMapsForge()
        if (AppConfig.getInstance().isRelease) {
            initAcra()
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

    private fun initAcra() {
        val builder = CoreConfigurationBuilder(this)
            .setBuildConfigClass(BuildConfig::class.java)
            .setReportFormat(StringFormat.KEY_VALUE_LIST)

        builder.getPluginConfigurationBuilder(MailSenderConfigurationBuilder::class.java)
            .setMailTo(AppConfig.getInstance().appContact)
            .setEnabled(true)

        builder.getPluginConfigurationBuilder(DialogConfigurationBuilder::class.java)
            .setTitle(AppConfig.getInstance().appName + " crashed")
            .setText(
                """
                This will open your e-mail app to send a crash report including some information about your device to "${AppConfig.getInstance().appContact}".
                This will help the author to fix and improve this app.
                """.trimIndent()
            )
            .setEnabled(true)
        ACRA.init(this, builder)
    }
}
