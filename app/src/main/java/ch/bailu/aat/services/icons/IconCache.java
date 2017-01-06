package ch.bailu.aat.services.icons;

import java.io.Closeable;

import ch.bailu.aat.gpx.GpxAttributes;
import ch.bailu.aat.gpx.interfaces.GpxPointInterface;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.ImageObject;
import ch.bailu.aat.services.cache.ImageObjectAbstract;
import ch.bailu.aat.services.cache.LockCache;
import ch.bailu.aat.services.cache.ObjectHandle;
import ch.bailu.aat.services.cache.SVGAssetImageObject;
import ch.bailu.aat.util.graphic.AppBitmap;

public class IconCache implements Closeable {
    private final LockCache<ImageObjectAbstract> icons = new LockCache(20);

    private final ServiceContext scontext;


    public IconCache(ServiceContext sc) {
        scontext = sc;
    }


    public AppBitmap getIcon(GpxPointInterface point, int size) {
        if (scontext.isUp()) {
            GpxAttributes attr = point.getAttributes();
            String path = scontext.getIconMapService().getSVGIconPath(attr);
            String iconFileID = SVGAssetImageObject.toID(path, size);

            ImageObjectAbstract icon = null;


            if (iconFileID != null && attr != null && scontext.isUp()) {

                icon = get(iconFileID);

                if (icon == null) {
                    icon = add(iconFileID, path, size);
                }
            }

            if (icon != null) {
                return icon.getAppBitmap();
            }
        }
        return null;
    }


/*
    public AppBitmap getIcon(GpxPointInterface point) {
        if (scontext.isUp()) {
            GpxAttributes attr = point.getAttributes();
            String iconFile = scontext.getIconMapService().getIconPath(attr);

            ImageObjectAbstract icon = null;


            if (iconFile != null && attr != null && scontext.isUp()) {

                icon = get(iconFile);

                if (icon == null) {
                    icon = add(iconFile);
                }
            }

            if (icon != null) {
                return icon.getAppBitmap();
            }
        }
        return null;
    }
*/


    private ImageObjectAbstract get(String id) {
        for (int i = 0; i < icons.size(); i++) {
            if (id.equals(icons.get(i).toString())) {
                return icons.use(i);
            }
        }
        return null;
    }


    private ImageObjectAbstract add(String id, String path, int size) {
        if (scontext.isUp()) {
            final ObjectHandle handle = scontext.getCacheService().
                    getObject(id, new SVGAssetImageObject.Factory(path, size));

            if (SVGAssetImageObject.class.isInstance(handle)) {
                final SVGAssetImageObject imageHandle = ((SVGAssetImageObject) handle);

                icons.add(imageHandle);
                return imageHandle;
            }
        }
        return null;

    }


    private ImageObjectAbstract add(String id) {

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
