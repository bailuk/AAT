package ch.bailu.aat;

import android.app.Application;

import org.mapsforge.map.android.graphics.AndroidGraphicFactory;

import ch.bailu.aat.map.mapsforge.MapsForgeContext;
import ch.bailu.aat.util.ui.AppLog;

public class App extends Application {

    @Override
    public void onCreate() {
        AppLog.d(this, "onCreate()");

        MapsForgeContext.initMapsForge(this);

        super.onCreate();
    }
}
