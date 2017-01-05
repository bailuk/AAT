package ch.bailu.aat.map.osmdroid.overlay.gpx;

import android.graphics.drawable.Drawable;

import java.io.Closeable;

import ch.bailu.aat.gpx.GpxAttributes;
import ch.bailu.aat.gpx.interfaces.GpxPointInterface;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.ImageObject;
import ch.bailu.aat.services.cache.LockCache;
import ch.bailu.aat.services.cache.ObjectHandle;
import ch.bailu.aat.util.graphic.AppBitmap;

public class IconCache implements Closeable {
    private final LockCache<ImageObject> icons = new LockCache(20);


    public AppBitmap getIcon(ServiceContext scontext, GpxPointInterface point) {
        if (scontext.isUp()) {
            GpxAttributes attr = point.getAttributes();
            String iconFile = scontext.getIconMapService().getIconPath(attr);

            ImageObject icon = null;


            if (iconFile != null && attr != null && scontext.isUp()) {

                icon = get(iconFile);

                if (icon == null) {
                    icon = add(scontext, iconFile);
                }
            }

            if (icon != null) {
                return icon.getAppBitmap();
            }
        }
        return null;
    }


    private ImageObject get(String id) {
        for (int i = 0; i < icons.size(); i++) {
            if (id.equals(icons.get(i).toString())) {
                return icons.use(i);
            }
        }
        return null;
    }


    private ImageObject add(ServiceContext scontext, String id) {
        if (scontext.isUp()) {
            final ObjectHandle handle = scontext.getCacheService().
                    getObject(id, new ImageObject.Factory());

            if (ImageObject.class.isInstance(handle)) {
                final ImageObject imageHandle = ((ImageObject) handle);

                icons.add(imageHandle);
                return imageHandle;
            }
        }
        return null;
    }




    @Override
    public void close() {
        icons.close();
    }
}
