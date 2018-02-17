package ch.bailu.aat;

import android.app.Application;

import ch.bailu.aat.map.mapsforge.MapsForgeContext;

public class App extends Application {

    @Override
    public void onCreate() {
        MapsForgeContext.initMapsForge(this);
        super.onCreate();
    }
}
