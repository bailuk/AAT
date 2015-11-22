package ch.bailu.aat.views.map;

import java.io.File;
import java.io.FileOutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import ch.bailu.aat.gpx.GpxFileWrapper;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.helpers.CleanUp;
import ch.bailu.aat.services.cache.CacheService;
import ch.bailu.aat.views.map.overlay.OsmOverlay;
import ch.bailu.aat.views.map.overlay.gpx.GpxDynOverlay;

public class OsmPreviewGenerator implements CleanUp {
    public static final int BITMAP_SIZE=128;

    private final OsmViewStatic map;
    private final Context context;
    
    private final PreviewTileProvider tileProvider;
    
    private final File imageFile;

    
    public OsmPreviewGenerator(CacheService loader, GpxList gpxList, File o)  {
        context=loader;
        imageFile=o;
        tileProvider = new PreviewTileProvider(loader);

        map = new OsmViewStatic(context, tileProvider);
        map.setDrawingCacheEnabled(true);
        
        final OsmOverlay[] overlays = new OsmOverlay[] {
                new GpxDynOverlay(map, loader, GpxInformation.ID.INFO_ID_FILEVIEW)
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
            AppBroadcaster.broadcast(context, 
                    AppBroadcaster.FILE_CHANGED_ONDISK, 
                    imageFile.getAbsolutePath(), 
                    getClass().getName());
            
        } catch (Exception e) {
            AppLog.e(context, e);
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
    public void cleanUp() {
        tileProvider.cleanUp();
    }
}
