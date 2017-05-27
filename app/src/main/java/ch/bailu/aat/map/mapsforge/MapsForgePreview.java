package ch.bailu.aat.map.mapsforge;


import android.graphics.Bitmap;
import android.graphics.Color;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Dimension;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.layer.Layer;
import org.mapsforge.map.util.MapPositionUtil;
import org.mapsforge.map.view.FrameBuffer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.map.MapDensity;
import ch.bailu.aat.map.layer.gpx.GpxDynLayer;
import ch.bailu.aat.map.tile.TileProviderStatic;
import ch.bailu.aat.map.tile.source.DownloadSource;
import ch.bailu.aat.map.tile.source.Source;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.graphic.SyncTileBitmap;
import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.simpleio.foc.Foc;

public class MapsForgePreview extends MapsForgeViewBase {
    private static final int BITMAP_SIZE=128;
    private static final Dimension DIM = new Dimension(BITMAP_SIZE, BITMAP_SIZE);
    private static final Source SOURCE = DownloadSource.MAPNIK;

    private final Foc               imageFile;
    private final TileProviderStatic provider;


    //private final int tileSize;
    private final MapPosition mapPosition;
    private final BoundingBox bounding;
    private final Point tlPoint;

    public MapsForgePreview(ServiceContext scontext, GpxInformation info, Foc out) {
        super(scontext, MapsForgePreview.class.getSimpleName(), new MapDensity());

        layout(0, 0, BITMAP_SIZE, BITMAP_SIZE);
        getModel().mapViewDimension.setDimension(DIM);

        imageFile = out;
        provider = new TileProviderStatic(scontext, SOURCE);

        MapsForgeTileLayer tileLayer = new MapsForgeTileLayer(scontext, provider);
        add(tileLayer, tileLayer);

        GpxDynLayer gpxLayer = new GpxDynLayer(getMContext());
        add(gpxLayer);

        enableLayers();

        gpxLayer.onContentUpdated(InfoID.FILEVIEW, info);
        frameBounding(info.getGpxList().getDelta().getBoundingBox());

        mapPosition  = getModel().mapViewPosition.getMapPosition();
        int tileSize = getModel().displayModel.getTileSize();
        bounding     = MapPositionUtil.getBoundingBox(mapPosition, DIM, tileSize);
        tlPoint      = MapPositionUtil.getTopLeftPoint(mapPosition, DIM, tileSize);

        tileLayer.preLoadTiles(bounding, mapPosition.zoomLevel, tlPoint);
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
        return new FrameBuffer(getModel().frameBufferModel, getModel().displayModel, AndroidGraphicFactory.INSTANCE) {
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


    public void generateBitmapFile() {
        SyncTileBitmap bitmap = generateBitmap();


        try {
            OutputStream outStream = imageFile.openW();
            bitmap.getAndroidBitmap().compress(Bitmap.CompressFormat.PNG, 90, outStream);
            outStream.close();
            AppBroadcaster.broadcast(getContext(),
                    AppBroadcaster.FILE_CHANGED_ONDISK,
                    imageFile.toString(),
                    getClass().getName());

        } catch (Exception e) {
            AppLog.e(getContext(), e);
        }

        bitmap.free();
    }



    public boolean isReady() {
        return provider.isReady();
    }


    @Override
    public void onDestroy() {
        provider.close();
        super.onDestroy();
    }
}
