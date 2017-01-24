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

import java.io.File;
import java.io.FileOutputStream;

import ch.bailu.aat.gpx.GpxFileWrapper;
import ch.bailu.aat.gpx.GpxList;
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

public class MapsForgePreview extends MapsForgeViewBase {
    public static final int BITMAP_SIZE=128;

    private final Source SOURCE = DownloadSource.MAPNIK;

    private final File imageFile;
    private final TileProviderStatic provider;



    public MapsForgePreview(ServiceContext sc, GpxList gpxList, File o) {
        super(sc, MapsForgePreview.class.getSimpleName(), new MapDensity());

        imageFile = o;

        provider = new TileProviderStatic(sc, SOURCE);
        MapsForgeTileLayer tileLayer = new MapsForgeTileLayer(provider, SOURCE.getAlpha());
        add(tileLayer, tileLayer);

        GpxDynLayer overlay = new GpxDynLayer(getMContext(), InfoID.FILEVIEW);
        add(overlay);

        enableLayers();
        layout(0, 0, BITMAP_SIZE, BITMAP_SIZE);

        overlay.onContentUpdated(InfoID.FILEVIEW, new GpxFileWrapper(o,gpxList));
        frameBounding(gpxList.getDelta().getBoundingBox());

        generateBitmap(BITMAP_SIZE).free();
    }


    public SyncTileBitmap generateBitmap(int size) {
        SyncTileBitmap bitmap = new SyncTileBitmap();

        bitmap.set(size, false);
        bitmap.getAndroidBitmap().eraseColor(Color.BLACK);


        Canvas drawingCanvas =
                AndroidGraphicFactory.createGraphicContext(bitmap.getAndroidCanvas());


        MapPosition mapPosition = getModel().mapViewPosition.getMapPosition();
        Dimension canvasDimension = drawingCanvas.getDimension();
        int tileSize = getModel().displayModel.getTileSize();

        BoundingBox boundingBox =
                MapPositionUtil.getBoundingBox(mapPosition, canvasDimension, tileSize);
        Point topLeftPoint =
                MapPositionUtil.getTopLeftPoint(mapPosition, canvasDimension, tileSize);

        for (Layer layer : getLayerManager().getLayers()) {
            if (layer.isVisible()) {
                layer.draw(boundingBox, mapPosition.zoomLevel, drawingCanvas, topLeftPoint);
            }
        }


        return bitmap;
    }

    public void generateBitmapFile() {
        SyncTileBitmap bitmap = generateBitmap(BITMAP_SIZE);


        try {
            final FileOutputStream outStream = new FileOutputStream(imageFile);
            bitmap.getAndroidBitmap().compress(Bitmap.CompressFormat.PNG, 90, outStream);
            outStream.close();
            AppBroadcaster.broadcast(getContext(),
                    AppBroadcaster.FILE_CHANGED_ONDISK,
                    imageFile.getAbsolutePath(),
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
