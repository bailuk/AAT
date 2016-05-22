package ch.bailu.aat.views.map;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import ch.bailu.aat.gpx.GpxFileWrapper;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.services.MultiServiceLink.ServiceContext;
import ch.bailu.aat.views.map.overlay.OsmOverlay;
import ch.bailu.aat.views.map.overlay.gpx.GpxDynOverlay;

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

        map = new OsmViewStatic(serviceContext.getContext(), tileProvider);
        map.setDrawingCacheEnabled(true);
        
        final OsmOverlay[] overlays = new OsmOverlay[] {
                new GpxDynOverlay(map, sc.getCacheService(), GpxInformation.ID.INFO_ID_FILEVIEW)
        };
        map.setOverlayList(overlays);
        
        map.layout(0, 0, BITMAP_SIZE, BITMAP_SIZE);

        map.updateGpxContent(new GpxFileWrapper(o,gpxList));      
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
