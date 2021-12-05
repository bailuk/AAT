package ch.bailu.aat.app;

import android.content.Context;

import ch.bailu.aat.dispatcher.AndroidBroadcaster;
import ch.bailu.aat.map.mapsforge.MapsForgePreview;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.preferences.map.AndroidSolidDem3Directory;
import ch.bailu.aat.preferences.map.AndroidSolidMapsForgeDirectory;
import ch.bailu.aat.preferences.map.AndroidSolidTileCacheDirectory;
import ch.bailu.aat.preferences.system.AndroidSolidDataDirectory;
import ch.bailu.aat.services.directory.AndroidSummaryConfig;
import ch.bailu.aat.util.graphic.SyncTileBitmap;
import ch.bailu.aat.util.sql.AndroidDatabase;
import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.dispatcher.Broadcaster;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.map.tile.MapTileInterface;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.preferences.map.SolidDem3Directory;
import ch.bailu.aat_lib.preferences.map.SolidMapsForgeDirectory;
import ch.bailu.aat_lib.preferences.map.SolidTileCacheDirectory;
import ch.bailu.aat_lib.preferences.system.SolidDataDirectory;
import ch.bailu.aat_lib.service.ServicesInterface;
import ch.bailu.aat_lib.service.background.DownloadConfig;
import ch.bailu.aat_lib.service.directory.MapPreviewInterface;
import ch.bailu.aat_lib.service.directory.SummaryConfig;
import ch.bailu.aat_lib.util.sql.Database;
import ch.bailu.foc.Foc;
import ch.bailu.foc.FocFactory;
import ch.bailu.foc_android.FocAndroidFactory;
import ch.bailu.foc_android.FocAssetFactory;

public class AndroidAppContext implements AppContext {
    private final Context context;
    private final Broadcaster broadcaster;
    private final ServicesInterface services;
    private final FocFactory assets;

    public AndroidAppContext(Context context, ServicesInterface serivces) {
        this.context = context;
        this.services = serivces;
        this.broadcaster = new AndroidBroadcaster(context);
        this.assets = new FocAssetFactory(context);
    }
    
    @Override
    public Broadcaster getBroadcaster() {
        return broadcaster;
    }

    @Override
    public ServicesInterface getServices() {
        return services;
    }

    @Override
    public StorageInterface getStorage() {
        return new Storage(context);
    }

    @Override
    public SummaryConfig getSummaryConfig() {
        return new AndroidSummaryConfig(context);
    }

    @Override
    public Database createDataBase() {
        return new AndroidDatabase(context);
    }

    @Override
    public MapPreviewInterface createMapPreview(GpxInformation info, Foc previewImageFile) {
        return new MapsForgePreview(context, this, info, previewImageFile);
    }

    @Override
    public MapTileInterface createMapTile() {
        return new SyncTileBitmap();
    }

    @Override
    public SolidDem3Directory getDem3Directory() {
        return new AndroidSolidDem3Directory(context);
    }

    @Override
    public DownloadConfig getDownloadConfig() {
        return new DownloadConfig(new AndroidAppConfig());
    }

    @Override
    public SolidDataDirectory getDataDirectory() {
        return new AndroidSolidDataDirectory(context);
    }

    @Override
    public FocFactory getAssets() {
        return assets;
    }

    @Override
    public SolidMapsForgeDirectory getMapDirectory() {
        return new AndroidSolidMapsForgeDirectory(context);
    }

    @Override
    public SolidTileCacheDirectory getTileCacheDirectory() {
        return new AndroidSolidTileCacheDirectory(context);
    }

    @Override
    public Foc toFoc(String string) {
        return new FocAndroidFactory(context).toFoc(string);
    }
}
