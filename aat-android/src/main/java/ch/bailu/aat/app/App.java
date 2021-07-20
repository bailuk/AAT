package ch.bailu.aat.app;

import android.app.Application;

import org.acra.ACRA;
import org.acra.config.CoreConfigurationBuilder;
import org.acra.config.DialogConfigurationBuilder;
import org.acra.config.MailSenderConfigurationBuilder;
import org.acra.data.StringFormat;
import org.mapsforge.core.util.Parameters;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;

import ch.bailu.aat.BuildConfig;
import ch.bailu.aat.dispatcher.AndroidBroadcaster;
import ch.bailu.aat.util.AndroidLogger;
import ch.bailu.aat_lib.app.AppConfig;
import ch.bailu.aat_lib.app.AppGraphicFactory;
import ch.bailu.aat_lib.dispatcher.AppBroadcaster;
import ch.bailu.aat_lib.logger.AppLog;
import ch.bailu.aat_lib.logger.BroadcastLogger;


public class App extends Application {

    @Override
    public void onCreate() {
        AppConfig.setInstance(new AndroidAppConfig());
        initLogger();
        initMapsForge();

        if (AppConfig.getInstance().isRelease()) {
            initAcra();
        }

        super.onCreate();
    }

    @Override
    public void onTerminate() {
        AppLog.set(new AndroidLogger());
        super.onTerminate();
    }


    private void initLogger() {
        AppLog.set(new AndroidLogger());
        AppLog.setError(new BroadcastLogger(
                new AndroidBroadcaster(getApplicationContext()),
                AppBroadcaster.LOG_ERROR,
                new AndroidLogger()));

        AppLog.setInfo(new BroadcastLogger(
                new AndroidBroadcaster(getApplicationContext()),
                AppBroadcaster.LOG_INFO,
                new AndroidLogger()));
    }


    private void initMapsForge() {
        AndroidGraphicFactory.createInstance(this);
        AppGraphicFactory.set(AndroidGraphicFactory.INSTANCE);
        Parameters.SQUARE_FRAME_BUFFER = false;
    }

    private void initAcra() {
        CoreConfigurationBuilder builder = new CoreConfigurationBuilder(this)
                .setBuildConfigClass(BuildConfig.class)
                .setReportFormat(StringFormat.KEY_VALUE_LIST);
        builder.getPluginConfigurationBuilder(MailSenderConfigurationBuilder.class)
                .setMailTo(AppConfig.getInstance().getContact())
                .setEnabled(true);
        builder.getPluginConfigurationBuilder(DialogConfigurationBuilder.class)
                .setTitle(AppConfig.getInstance().getShortName() + " crashed")
                .setText(
                        "This will open your e-mail app to send a crash report " +
                        "including some information about your device to \"" +
                        AppConfig.getInstance().getContact() +
                        "\".\n" +
                        "This will help the author to fix and improve this app.")

                .setEnabled(true);
        ACRA.init(this, builder);
    }
}
