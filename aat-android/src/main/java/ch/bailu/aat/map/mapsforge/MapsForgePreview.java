package ch.bailu.aat.map.mapsforge;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Dimension;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.layer.Layer;
import org.mapsforge.map.layer.TilePosition;
import org.mapsforge.map.util.LayerUtil;
import org.mapsforge.map.util.MapPositionUtil;
import org.mapsforge.map.view.FrameBuffer;
import org.mapsforge.map.view.FrameBufferHA2;

import java.io.OutputStream;
import java.util.List;

import ch.bailu.aat.map.MapDensity;
import ch.bailu.aat.map.tile.TileProvider;
import ch.bailu.aat.map.tile.source.CacheOnlySource;
import ch.bailu.aat_lib.service.cache.DownloadSource;
import ch.bailu.aat_lib.map.tile.source.MapsForgeSource;
import ch.bailu.aat_lib.map.tile.source.Source;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.preferences.map.SolidMapTileStack;
import ch.bailu.aat_lib.preferences.map.SolidRenderTheme;
import ch.bailu.aat.util.OldAppBroadcaster;
import ch.bailu.aat.util.graphic.SyncTileBitmap;
import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.dispatcher.AppBroadcaster;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.logger.AppLog;
import ch.bailu.aat_lib.map.layer.gpx.GpxDynLayer;
import ch.bailu.aat_lib.service.directory.MapPreviewInterface;
import ch.bailu.foc.Foc;

public class MapsForgePreview extends MapsForgeViewBase implements MapPreviewInterface {
    private static final int BITMAP_SIZE=128;
    private static final Dimension DIM = new Dimension(BITMAP_SIZE, BITMAP_SIZE);
    private static final Source MAPNIK = new CacheOnlySource(DownloadSource.MAPNIK);

    private final Foc          imageFile;
    private final TileProvider provider;


    private final MapPosition mapPosition;
    private final BoundingBox bounding;
    private final Point tlPoint;

    public MapsForgePreview(Context context, AppContext appContext, GpxInformation info, Foc out) throws IllegalArgumentException {
        super(context, appContext.getServices(), MapsForgePreview.class.getSimpleName(), new MapDensity());



        layout(0, 0, BITMAP_SIZE, BITMAP_SIZE);
        getModel().mapViewDimension.setDimension(DIM);

        imageFile = out;
        provider = new TileProvider(appContext, getSource(new SolidRenderTheme(appContext.getMapDirectory(), appContext)));

        MapsForgeTileLayer tileLayer = new MapsForgeTileLayer(appContext.getServices(), provider);
        add(tileLayer, tileLayer);

        GpxDynLayer gpxLayer = new GpxDynLayer(new Storage(getContext()), getMContext(), appContext.getServices());
        add(gpxLayer);

        attachLayers();

        gpxLayer.onContentUpdated(InfoID.FILEVIEW, info);
        frameBounding(info.getGpxList().getDelta().getBoundingBox());

        mapPosition = getModel().mapViewPosition.getMapPosition();
        int tileSize = getModel().displayModel.getTileSize();
        bounding = MapPositionUtil.getBoundingBox(mapPosition, DIM, tileSize);
        tlPoint = MapPositionUtil.getTopLeftPoint(mapPosition, DIM, tileSize);


        preLoadTiles();

    }

    private void preLoadTiles() {
        List<TilePosition> tilePositions = LayerUtil.getTilePositions(bounding,
                mapPosition.zoomLevel, tlPoint,
                getModel().displayModel.getTileSize());

        provider.onAttached();
        provider.preload(tilePositions);
    }


    private static Source getSource(SolidRenderTheme stheme) {
        SolidMapTileStack tiles = new SolidMapTileStack(stheme);

        boolean[] enabled = tiles.getEnabledArray();

        if (enabled[0]) {

            String theme = stheme.getValueAsString();


            MapsForgeSource mfs = new MapsForgeSource(theme);
            return new CacheOnlySource(mfs);
        }

        return MAPNIK;
    }


    /**
     *
     * Begin of "prevent MapView from drawing" hack
     * FIXME:
     *   This hack prevents the map view from calling the layers draw() function.
     *   The correct implementation would be to port the preview generator away from the MapView and
     *   just use the MapViews model.
     */

    @Override
    public FrameBuffer getFrameBuffer() {
        // TODO: change this to HA3 when updating to MapsForge 0.15
        return new FrameBufferHA2(getModel().frameBufferModel, getModel().displayModel, AndroidGraphicFactory.INSTANCE) {
            @Override
            public org.mapsforge.core.graphics.Bitmap getDrawingBitmap() {
                return null;
            }
        };
    }

    @Override
    public void repaint() {}

    @Override
    public void requestRedraw() {}
    /**
     * End of "prevent MapView from drawing" hack
     */


    private SyncTileBitmap generateBitmap() {
        final SyncTileBitmap bitmap = new SyncTileBitmap();

        bitmap.set(BITMAP_SIZE, false);
        if (bitmap.getAndroidBitmap() != null) {
            final android.graphics.Canvas c = bitmap.getAndroidCanvas();
            final Canvas canvas = AndroidGraphicFactory.createGraphicContext(c);

            bitmap.getAndroidBitmap().eraseColor(Color.BLACK);

            for (Layer layer : getLayerManager().getLayers()) {
                layer.draw(bounding, mapPosition.zoomLevel, canvas, tlPoint);
            }

        }
        //drawingCanvas.destroy();
        return bitmap;
    }


    @SuppressLint("WrongThread")
    public void generateBitmapFile() {
        SyncTileBitmap bitmap = generateBitmap();


        try {
            OutputStream outStream = imageFile.openW();
            bitmap.getAndroidBitmap().compress(Bitmap.CompressFormat.PNG, 90, outStream);
            outStream.close();
            OldAppBroadcaster.broadcast(getContext(),
                    AppBroadcaster.FILE_CHANGED_ONDISK,
                    imageFile.toString(),
                    getClass().getName());

        } catch (Exception e) {
            AppLog.e(getContext(), e);
        }

        bitmap.free();
    }



    public boolean isReady() {
        return provider.isReadyAndLoaded();
    }


    @Override
    public void onDestroy() {
        provider.onDetached();
        super.onDestroy();
    }

}
