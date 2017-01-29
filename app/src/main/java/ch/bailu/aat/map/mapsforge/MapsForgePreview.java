package ch.bailu.aat.map.mapsforge;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Dimension;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.layer.Layer;
import org.mapsforge.map.util.MapPositionUtil;

import java.io.File;
import java.io.FileOutputStream;

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

public class MapsForgePreview extends MapsForgeViewBase {
    private static final int BITMAP_SIZE=128;
    private static final Dimension DIM = new Dimension(BITMAP_SIZE, BITMAP_SIZE);
    private static final Source SOURCE = DownloadSource.MAPNIK;

    private final File               imageFile;
    private final TileProviderStatic provider;


    private final int tileSize;
    private final MapPosition mapPosition;
    private final BoundingBox bounding;
    private final Point tlPoint;

    public MapsForgePreview(ServiceContext scontext, GpxInformation info, File out) {
        super(scontext, MapsForgePreview.class.getSimpleName(), new MapDensity());

        layout(0, 0, BITMAP_SIZE, BITMAP_SIZE);
        getModel().mapViewDimension.setDimension(DIM);

        imageFile = out;
        provider = new TileProviderStatic(scontext, SOURCE);

        MapsForgeTileLayer tileLayer = new MapsForgeTileLayer(provider, SOURCE.getAlpha());
        add(tileLayer, tileLayer);

        GpxDynLayer gpxLayer = new GpxDynLayer(getMContext(), InfoID.FILEVIEW);
        add(gpxLayer);

        enableLayers();


        gpxLayer.onContentUpdated(InfoID.FILEVIEW, info);
        frameBounding(info.getGpxList().getDelta().getBoundingBox());

        mapPosition = getModel().mapViewPosition.getMapPosition();
        tileSize    = getModel().displayModel.getTileSize();
        bounding    = MapPositionUtil.getBoundingBox(mapPosition, DIM, tileSize);
        tlPoint     = MapPositionUtil.getTopLeftPoint(mapPosition, DIM, tileSize);

        tileLayer.preLoadTiles(bounding, mapPosition.zoomLevel, tlPoint);
    }

    @Override
    public void repaint() {}


    public SyncTileBitmap generateBitmap() {
        SyncTileBitmap bitmap = new SyncTileBitmap();

        bitmap.set(BITMAP_SIZE, false);
        bitmap.getAndroidBitmap().eraseColor(Color.BLACK);

        android.graphics.Canvas canvas = bitmap.getAndroidCanvas();
        Canvas drawingCanvas =
                AndroidGraphicFactory.createGraphicContext(canvas);

        AppLog.d(this, "Generate");

        for (Layer layer : getLayerManager().getLayers()) {
            AppLog.d(this, "L draw");
            layer.draw(bounding, mapPosition.zoomLevel, drawingCanvas, tlPoint);
        }

        drawingCanvas.destroy();
        return bitmap;
    }


    public void generateBitmapFile() {
        SyncTileBitmap bitmap = generateBitmap();

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
