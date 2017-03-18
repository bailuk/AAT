package ch.bailu.aat;

import android.app.Application;

import org.mapsforge.map.android.graphics.AndroidGraphicFactory;

import ch.bailu.aat.util.ui.AppLog;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AppLog.d(this, "onCreate()");

        // TODO move this to MapForge module
        AndroidGraphicFactory.createInstance(this);
    }
}
