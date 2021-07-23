package ch.bailu.aat_awt.map;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.util.LatLongUtils;
import org.mapsforge.core.util.Parameters;
import org.mapsforge.map.awt.graphics.AwtGraphicFactory;
import org.mapsforge.map.awt.util.AwtUtil;
import org.mapsforge.map.awt.view.MapView;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.datastore.MultiMapDataStore;
import org.mapsforge.map.layer.Layers;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.debug.TileCoordinatesLayer;
import org.mapsforge.map.layer.debug.TileGridLayer;
import org.mapsforge.map.layer.download.TileDownloadLayer;
import org.mapsforge.map.layer.download.tilesource.OpenStreetMapMapnik;
import org.mapsforge.map.layer.download.tilesource.TileSource;
import org.mapsforge.map.layer.hills.HillsRenderConfig;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.model.IMapViewPosition;
import org.mapsforge.map.model.Model;
import org.mapsforge.map.model.common.Observer;
import org.mapsforge.map.reader.MapFile;
import org.mapsforge.map.rendertheme.InternalRenderTheme;

import java.awt.Graphics;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ch.bailu.aat_lib.app.AppGraphicFactory;
import ch.bailu.aat_lib.coordinates.BoundingBoxE6;
import ch.bailu.aat_lib.dispatcher.DispatcherInterface;
import ch.bailu.aat_lib.map.AppDensity;
import ch.bailu.aat_lib.map.MapContext;
import ch.bailu.aat_lib.map.MapViewInterface;
import ch.bailu.aat_lib.map.layer.MapLayerInterface;
import ch.bailu.aat_lib.map.layer.MapPositionLayer;
import ch.bailu.aat_lib.preferences.OnPreferencesChanged;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.util.Limit;

public class AwtCustomMapView extends MapView implements MapViewInterface, OnPreferencesChanged {
    private static final boolean SHOW_DEBUG_LAYERS = false;
    private static final boolean SHOW_RASTER_MAP = false;

    private final AwtMapContext backgroundContext;
    private final AwtMapContextForeground foregroundContext;

    private final MapPositionLayer pos;

    private final ArrayList<MapLayerInterface> layers = new ArrayList<>(10);

    final BoundingBox boundingBox;

    final StorageInterface storage;
    public AwtCustomMapView(StorageInterface storage, List<File> mapFiles, DispatcherInterface dispatcher) {

        this.storage = storage;

        backgroundContext = new AwtMapContext(this, this.getClass().getSimpleName());
        foregroundContext = new AwtMapContextForeground(this, backgroundContext, new AppDensity(), layers);

        addLayer(backgroundContext);

        getMapScaleBar().setVisible(false);
        if (SHOW_DEBUG_LAYERS) {
            getFpsCounter().setVisible(true);
        }

        Parameters.SQUARE_FRAME_BUFFER = false;
        boundingBox = addLayers(this, mapFiles, null);

        pos = new MapPositionLayer(getMContext(), storage, dispatcher);
        add(pos);

        getModel().mapViewPosition.addObserver(new Observer() {
            private LatLong center = getModel().mapViewPosition.getCenter();

            @Override
            public void onChange() {
                LatLong newCenter = getModel().mapViewPosition.getCenter();

                if (newCenter != null && newCenter.equals(center) == false) {
                    center = newCenter;
                    pos.onMapCenterChanged(center);
                }
            }
        });

        attach();
    }



    private static BoundingBox addLayers(MapView mapView, List<File> mapFiles, HillsRenderConfig hillsRenderConfig) {
        Layers layers = mapView.getLayerManager().getLayers();

        int tileSize = 256;

        // Tile cache
        TileCache tileCache = AwtUtil.createTileCache(
                tileSize,
                mapView.getModel().frameBufferModel.getOverdrawFactor(),
                1024,
                new File(System.getProperty("java.io.tmpdir"), UUID.randomUUID().toString()));

        final BoundingBox boundingBox;
        if (SHOW_RASTER_MAP) {
            // Raster
            mapView.getModel().displayModel.setFixedTileSize(tileSize);
            OpenStreetMapMapnik tileSource = OpenStreetMapMapnik.INSTANCE;
            tileSource.setUserAgent("mapsforge-samples-awt");
            TileDownloadLayer tileDownloadLayer = createTileDownloadLayer(tileCache, mapView.getModel().mapViewPosition, tileSource);
            layers.add(tileDownloadLayer);
            tileDownloadLayer.start();
            mapView.setZoomLevelMin(tileSource.getZoomLevelMin());
            mapView.setZoomLevelMax(tileSource.getZoomLevelMax());
            boundingBox = new BoundingBox(LatLongUtils.LATITUDE_MIN, LatLongUtils.LONGITUDE_MIN, LatLongUtils.LATITUDE_MAX, LatLongUtils.LONGITUDE_MAX);
        } else {
            // Vector
            mapView.getModel().displayModel.setFixedTileSize(tileSize);
            MultiMapDataStore mapDataStore = new MultiMapDataStore(MultiMapDataStore.DataPolicy.RETURN_ALL);
            for (File file : mapFiles) {
                mapDataStore.addMapDataStore(new MapFile(file), false, false);
            }
            TileRendererLayer tileRendererLayer = createTileRendererLayer(tileCache, mapDataStore, mapView.getModel().mapViewPosition, hillsRenderConfig);
            layers.add(tileRendererLayer);
            boundingBox = mapDataStore.boundingBox();
        }

        // Debug
        if (SHOW_DEBUG_LAYERS) {
            layers.add(new TileGridLayer(AppGraphicFactory.instance(), mapView.getModel().displayModel));
            layers.add(new TileCoordinatesLayer(AppGraphicFactory.instance(), mapView.getModel().displayModel));
        }

        return boundingBox;
    }


    @SuppressWarnings("unused")
    private static TileDownloadLayer createTileDownloadLayer(TileCache tileCache, IMapViewPosition mapViewPosition, TileSource tileSource) {
        return new TileDownloadLayer(tileCache, mapViewPosition, tileSource, AppGraphicFactory.instance()) {
            @Override
            public boolean onTap(LatLong tapLatLong, Point layerXY, Point tapXY) {
                System.out.println("Tap on: " + tapLatLong);
                return true;
            }
        };
    }

    private static TileRendererLayer createTileRendererLayer(TileCache tileCache, MapDataStore mapDataStore, IMapViewPosition mapViewPosition, HillsRenderConfig hillsRenderConfig) {
        TileRendererLayer tileRendererLayer = new TileRendererLayer(tileCache, mapDataStore, mapViewPosition, false, true, false, AppGraphicFactory.instance(), hillsRenderConfig) {
            @Override
            public boolean onTap(LatLong tapLatLong, Point layerXY, Point tapXY) {
                System.out.println("Tap on: " + tapLatLong);
                return true;
            }
        };
        tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.DEFAULT);
        return tileRendererLayer;
    }


    public void showMap() {
        final Model model = getModel();

        byte zoomLevel = LatLongUtils.zoomForBounds(model.mapViewDimension.getDimension(), boundingBox, model.displayModel.getTileSize());
        model.mapViewPosition.setMapPosition(new MapPosition(boundingBox.getCenterPoint(), zoomLevel));
    }



    @Override
    public void frameBounding(BoundingBoxE6 boundingBox) {

    }

    @Override
    public void zoomOut() {
        setZoomLevel((byte) Limit.clamp(
                getModel().mapViewPosition.getZoomLevel()-1,
                getModel().mapViewPosition.getZoomLevelMin(),
                getModel().mapViewPosition.getZoomLevelMax()));
    }

    @Override
    public void zoomIn() {
        setZoomLevel((byte) Limit.clamp(
                getModel().mapViewPosition.getZoomLevel()+1,
                getModel().mapViewPosition.getZoomLevelMin(),
                getModel().mapViewPosition.getZoomLevelMax()));
    }

    @Override
    public void requestRedraw() {
        repaint();
    }

    @Override
    public void add(MapLayerInterface l) {
        addLayer(new AwtLayerWrapper(backgroundContext, l));
        layers.add(l);
    }

    @Override
    public MapContext getMContext() {
        return backgroundContext;
    }

    @Override
    public void reDownloadTiles() {

    }

    @Override
    public IMapViewPosition getMapViewPosition() {
        return getModel().mapViewPosition;
    }


    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        Canvas canvas = (Canvas) AwtGraphicFactory.createGraphicContext(graphics);
        foregroundContext.dispatchDraw(canvas, getWidth(), getHeight());
    }

    public void attach() {
        storage.register(this);
        for (MapLayerInterface layer : layers) {
            layer.onAttached();
        }
    }

    public void detach() {
        storage.unregister(this);
        for (MapLayerInterface layer : layers) {
            layer.onDetached();
        }
    }

    @Override
    public void onPreferencesChanged(StorageInterface storage, String key) {
        for (MapLayerInterface layer : layers) {
            layer.onPreferencesChanged(storage, key);
        }

    }
}
