package ch.bailu.aat.views.map.overlay.gpx;

import java.io.Closeable;

import android.graphics.drawable.Drawable;
import ch.bailu.aat.gpx.GpxAttributes;
import ch.bailu.aat.gpx.interfaces.GpxPointInterface;
import ch.bailu.aat.services.cache.CacheService;
import ch.bailu.aat.services.cache.FreeLater;
import ch.bailu.aat.services.cache.ImageObject;
import ch.bailu.aat.services.icons.IconMapService;

public class MapIconCache implements Closeable {
    private FreeLater current = new FreeLater();
    private FreeLater old = new FreeLater();
    
    // TODO add SparseArray with non existing files (for speed)
    
    
    public void newPass() {
        FreeLater _old = old;
        old = current;
        
        current=_old;
        current.freeAll();
    }

    
    public Drawable getIcon(CacheService cache, GpxPointInterface point) {
        
        Drawable drawable=null;
        
        GpxAttributes a = point.getAttributes();
        if (a != null) {
            
            String fileID = a.get(IconMapService.KEY_ICON_SMALL);
            if (fileID != null) {
                drawable = getIcon(cache, fileID);
            }
        }
        return drawable;
    }
    
    
    public Drawable getIcon(CacheService cache, String fileID) {
        Drawable drawable=null;
        
        ImageObject imageObject =  (ImageObject) cache.getObject(fileID, new ImageObject.Factory());
        if (imageObject != null) {
            
            drawable = imageObject.getDrawable();
            current.freeLater(imageObject);
        }
        return drawable;
    }


    @Override
    public void close() {
        current.close();
        old.close();
    }
}
