package ch.bailu.aat;

import android.app.Application;

import org.acra.ACRA;
import org.acra.config.CoreConfigurationBuilder;
import org.acra.config.DialogConfigurationBuilder;
import org.acra.config.MailSenderConfigurationBuilder;
import org.acra.data.StringFormat;

import ch.bailu.aat.map.mapsforge.MapsForgeContext;
import ch.bailu.aat.util.ui.AppConfig;


public class App extends Application {



    @Override
    public void onCreate() {
        initMapsForge();
        //initAcra();
        super.onCreate();
    }


    private void initMapsForge() {
        MapsForgeContext.initMapsForge(this);
    }

    private void initAcra() {
        CoreConfigurationBuilder builder = new CoreConfigurationBuilder(this)
                .setBuildConfigClass(BuildConfig.class)
                .setReportFormat(StringFormat.KEY_VALUE_LIST);
        builder.getPluginConfigurationBuilder(MailSenderConfigurationBuilder.class)
                .setMailTo(AppConfig.getContact())
                .setEnabled(true);
        builder.getPluginConfigurationBuilder(DialogConfigurationBuilder.class)
                .setTitle(AppConfig.getShortName() + " crashed")
                .setText(
                        "This will open your e-mail app to send a crash report " +
                        "including some information about your device to \"" +
                        AppConfig.getContact() +
                        "\".\n" +
                        "This will help the author to fix and improve this app.")

                .setEnabled(true);
        ACRA.init(this, builder);

    }
}
