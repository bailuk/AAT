package ch.bailu.aat;

import android.app.Application;
import android.content.Context;
import android.provider.ContactsContract;

import org.acra.ACRA;
import org.acra.annotation.AcraCore;
import org.acra.annotation.AcraDialog;
import org.acra.annotation.AcraMailSender;
import org.acra.config.CoreConfigurationBuilder;
import org.acra.config.DialogConfigurationBuilder;
import org.acra.config.MailSenderConfigurationBuilder;
import org.acra.data.StringFormat;
import org.acra.dialog.CrashReportDialog;

import ch.bailu.aat.map.mapsforge.MapsForgeContext;
import ch.bailu.aat.util.ToDo;
import ch.bailu.aat.util.ui.AppLog;


public class App extends Application {

    @Override
    public void onCreate() {
        initMapsForge();
        initAcra();
        super.onCreate();
    }


    private void initMapsForge() {
        MapsForgeContext.initMapsForge(this);
    }

    private void initAcra() {
        CoreConfigurationBuilder builder = new CoreConfigurationBuilder(this)
                .setBuildConfigClass(BuildConfig.class)
                .setReportFormat(StringFormat.JSON);
        builder.getPluginConfigurationBuilder(MailSenderConfigurationBuilder.class)
                .setMailTo("aat@bailu.ch")
                .setEnabled(true);
        builder.getPluginConfigurationBuilder(DialogConfigurationBuilder.class)
                .setText(ToDo.translate("AAT Crashed"))
                .setEnabled(true);
        ACRA.init(this, builder);

    }
}
