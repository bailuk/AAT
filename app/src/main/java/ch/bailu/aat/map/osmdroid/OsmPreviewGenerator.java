package ch.bailu.aat.map.osmdroid;

import android.graphics.Bitmap;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;

import ch.bailu.aat.gpx.GpxFileWrapper;
import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.map.MapDensity;
import ch.bailu.aat.map.layer.gpx.GpxDynLayer;
import ch.bailu.aat.map.tile.TileProviderPreview;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.ui.AppLog;

public class OsmPreviewGenerator implements Closeable {
    public static final int BITMAP_SIZE=128;


    private final OsmViewStatic map;
    private final ServiceContext serviceContext;
    
    private final TileProviderPreview tileProvider;
    
    private final File imageFile;

    
    public OsmPreviewGenerator(ServiceContext sc, GpxList gpxList, File o)  {
        serviceContext=sc;
        imageFile=o;
        tileProvider = new TileProviderPreview(sc);



        map = new OsmViewStatic(serviceContext, tileProvider, new MapDensity());
        map.setDrawingCacheEnabled(true);
        
        GpxDynLayer overlay = new GpxDynLayer(map.getMContext(), InfoID.FILEVIEW);
        map.add(overlay);

        map.layout(0, 0, BITMAP_SIZE, BITMAP_SIZE);

        overlay.onContentUpdated(InfoID.FILEVIEW, new GpxFileWrapper(o,gpxList));
        map.frameBounding(gpxList.getDelta().getBoundingBox());
        map.getDrawingCache(false);
    }



    public void generateBitmapFile() {
        Bitmap bitmap=generateBitmap();
        try {
            final FileOutputStream outStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, outStream);
            outStream.close();
            AppBroadcaster.broadcast(serviceContext.getContext(), 
                    AppBroadcaster.FILE_CHANGED_ONDISK, 
                    imageFile.getAbsolutePath(), 
                    getClass().getName());
            
        } catch (Exception e) {
            AppLog.e(serviceContext.getContext(), e);
        }
    }

    
    private Bitmap generateBitmap() {
        map.invalidate();
        return map.getDrawingCache(false);
    }

    public boolean isReady() {
        return tileProvider.isReady();
    }


    @Override
    public void close() {
        tileProvider.close();
    }
}
