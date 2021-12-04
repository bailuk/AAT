package ch.bailu.aat.activities;

import ch.bailu.aat.app.AndroidAppConfig;
import ch.bailu.aat.factory.AndroidFocFactory;
import ch.bailu.aat.map.mapsforge.MapsForgePreview;
import ch.bailu.aat.preferences.map.AndroidSolidDem3Directory;
import ch.bailu.aat.preferences.map.AndroidSolidMapsForgeDirectory;
import ch.bailu.aat.preferences.map.AndroidSolidTileCacheDirectory;
import ch.bailu.aat.preferences.system.AndroidSolidDataDirectory;
import ch.bailu.aat.services.directory.AndroidSummaryConfig;
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
import ch.bailu.aat_lib.resources.AssetsInterface;
import ch.bailu.aat_lib.service.ServicesInterface;
import ch.bailu.aat_lib.service.background.DownloadConfig;
import ch.bailu.aat_lib.service.directory.MapPreviewInterface;
import ch.bailu.aat_lib.service.directory.SummaryConfig;
import ch.bailu.aat_lib.util.sql.Database;
import ch.bailu.foc.Foc;

public abstract class ActivityContext extends AbsDispatcher {

    public AppContext getAppContext() {
        return new AppContext() {
            @Override
            public Broadcaster getBroadcaster() {
                return ActivityContext.this.getBroadcaster();
            }

            @Override
            public ServicesInterface getServices() {
                return ActivityContext.this.getServiceContext();
            }

            @Override
            public StorageInterface getStorage() {
                return ActivityContext.this.getStorage();
            }

            @Override
            public SummaryConfig getSummaryConfig() {
                return new AndroidSummaryConfig(ActivityContext.this);
            }

            @Override
            public Database createDataBase() {
                return new AndroidDatabase(ActivityContext.this);
            }

            @Override
            public MapPreviewInterface createMapPreview(GpxInformation info, Foc previewImageFile) {
                return new MapsForgePreview(ActivityContext.this.getServiceContext(), this, info, previewImageFile);
            }

            @Override
            public MapTileInterface createMapTile() {
                return null;
            }

            @Override
            public SolidDem3Directory getDem3Directory() {
                return new AndroidSolidDem3Directory(ActivityContext.this);
            }

            @Override
            public DownloadConfig getDownloadConfig() {
                return new DownloadConfig(new AndroidAppConfig());
            }

            @Override
            public SolidDataDirectory getDataDirectory() {
                return new AndroidSolidDataDirectory(ActivityContext.this);
            }

            @Override
            public AssetsInterface getAssets() {
                return null;
            }

            @Override
            public SolidMapsForgeDirectory getMapDirectory() {
                return new AndroidSolidMapsForgeDirectory(ActivityContext.this);
            }

            @Override
            public SolidTileCacheDirectory getTileCacheDirectory() {
                return new AndroidSolidTileCacheDirectory(ActivityContext.this);
            }

            @Override
            public Foc toFoc(String string) {
                return new AndroidFocFactory(ActivityContext.this).toFoc(string);
            }
        };
    }
}
