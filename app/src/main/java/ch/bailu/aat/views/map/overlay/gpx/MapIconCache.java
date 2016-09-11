package ch.bailu.aat.views.map.overlay.gpx;

import java.io.Closeable;

import android.graphics.drawable.Drawable;
import ch.bailu.aat.gpx.GpxAttributes;
import ch.bailu.aat.gpx.interfaces.GpxPointInterface;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.FreeLater;
import ch.bailu.aat.services.cache.ImageObject;
import ch.bailu.aat.services.cache.ObjectHandle;
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

    
    public Drawable getBigIcon(ServiceContext scontext, GpxPointInterface point) {
        return getIcon(scontext, point, IconMapService.KEY_ICON_BIG);
    }
    
    
    public Drawable getSmallIcon(ServiceContext scontext, GpxPointInterface point) {
        return getIcon(scontext, point, IconMapService.KEY_ICON_SMALL);
    }
    
    
    
    public static String getBigIconFileName(GpxPointInterface point) {
        return getIconFileName(point, IconMapService.KEY_ICON_BIG);
    }
    
    public static String getIconFileName(GpxPointInterface point, String key) {
        String fileID=null;
        
        GpxAttributes a = point.getAttributes();
        if (a != null) {
            
            fileID = a.get(key);
        }
        return fileID;
    }
    
    
    private Drawable getIcon(ServiceContext scontext, GpxPointInterface point, String key) {
        
        Drawable drawable=null;
        
        String fileID=getIconFileName(point, key);
        if (fileID != null) {
            drawable = getIcon(scontext, fileID);
        }
        return drawable;
    }

    
    public Drawable getIcon(ServiceContext scontext, String fileID) {
        Drawable drawable=null;
        
        final ObjectHandle handle =  scontext.getCacheService().getObject(fileID, new ImageObject.Factory());
        if (ImageObject.class.isInstance(handle) ) {
            
            drawable = ((ImageObject)handle).getDrawable();
            current.freeLater(handle);
        }
        return drawable;
    }


    @Override
    public void close() {
        current.close();
        old.close();
    }
}
