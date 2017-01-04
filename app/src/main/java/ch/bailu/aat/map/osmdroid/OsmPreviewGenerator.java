package ch.bailu.aat.map.osmdroid;

import android.graphics.Bitmap;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;

import ch.bailu.aat.gpx.GpxFileWrapper;
import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.map.osmdroid.overlay.gpx.GpxDynOverlay;

public class OsmPreviewGenerator implements Closeable {
    public static final int BITMAP_SIZE=128;


    private final OsmViewStatic map;
    private final ServiceContext serviceContext;
    
    private final PreviewTileProvider tileProvider;
    
    private final File imageFile;

    
    public OsmPreviewGenerator(ServiceContext sc, GpxList gpxList, File o)  {
        serviceContext=sc;
        imageFile=o;
        tileProvider = new PreviewTileProvider(sc);



        map = new OsmViewStatic(serviceContext.getContext(), tileProvider, new MapDensity());
        map.setDrawingCacheEnabled(true);
        
        GpxDynOverlay overlay = new GpxDynOverlay(map, sc, InfoID.FILEVIEW);
        map.add(overlay);

        map.layout(0, 0, BITMAP_SIZE, BITMAP_SIZE);

        overlay.onContentUpdated(InfoID.FILEVIEW, new GpxFileWrapper(o,gpxList));
        map.frameBoundingBox(gpxList.getDelta().getBoundingBox());
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
