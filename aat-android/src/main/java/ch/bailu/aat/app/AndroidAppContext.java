package ch.bailu.aat.app;

import android.content.Context;

import ch.bailu.aat.dispatcher.AndroidBroadcaster;
import ch.bailu.aat.map.mapsforge.MapsForgePreview;
import ch.bailu.aat.map.tile.AndroidTilePainter;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.preferences.map.AndroidMapDirectories;
import ch.bailu.aat.preferences.map.AndroidSolidDem3Directory;
import ch.bailu.aat.preferences.map.AndroidSolidTileCacheDirectory;
import ch.bailu.aat.preferences.system.AndroidSolidDataDirectory;
import ch.bailu.aat.services.directory.AndroidSummaryConfig;
import ch.bailu.aat.util.AndroidTimer;
import ch.bailu.aat.util.graphic.AndroidSyncTileBitmap;
import ch.bailu.aat.util.sql.AndroidDbConnection;
import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.dispatcher.Broadcaster;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.map.TilePainter;
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
import ch.bailu.aat_lib.util.Timer;
import ch.bailu.aat_lib.util.sql.DbConnection;
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
    public DbConnection createDataBase() {
        return new AndroidDbConnection(context);
    }

    @Override
    public MapPreviewInterface createMapPreview(GpxInformation info, Foc previewImageFile) {
        return new MapsForgePreview(context, this, info, previewImageFile);
    }

    @Override
    public MapTileInterface createMapTile() {
        return new AndroidSyncTileBitmap();
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
        return new AndroidMapDirectories(context).createSolidDirectory();
    }

    @Override
    public SolidTileCacheDirectory getTileCacheDirectory() {
        return new AndroidSolidTileCacheDirectory(context);
    }

    @Override
    public Timer createTimer() {
        return new AndroidTimer();
    }

    @Override
    public TilePainter getTilePainter() {
        return new AndroidTilePainter();
    }

    @Override
    public Foc toFoc(String string) {
        return new FocAndroidFactory(context).toFoc(string);
    }
}
