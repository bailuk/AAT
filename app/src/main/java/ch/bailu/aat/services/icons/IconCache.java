package ch.bailu.aat.services.icons;

import android.graphics.Bitmap;

import java.io.Closeable;

import ch.bailu.aat.gpx.GpxAttributes;
import ch.bailu.aat.gpx.interfaces.GpxPointInterface;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.ImageObject;
import ch.bailu.aat.services.cache.ImageObjectAbstract;
import ch.bailu.aat.services.cache.LockCache;
import ch.bailu.aat.services.cache.ObjectHandle;
import ch.bailu.aat.services.cache.SVGAssetImageObject;

public class IconCache implements Closeable {
    private final LockCache<ImageObjectAbstract> icons = new LockCache(20);

    private final ServiceContext scontext;


    public IconCache(ServiceContext sc) {
        scontext = sc;
    }


    public Bitmap getIcon(GpxPointInterface point, int size) {
        Bitmap r = null;

        if (scontext.lock()) {
            GpxAttributes attr = point.getAttributes();
            String path = scontext.getIconMapService().getSVGIconPath(attr);
            String iconFileID = SVGAssetImageObject.toID(path, size);

            ImageObjectAbstract icon = null;


            if (iconFileID != null && attr != null) {

                icon = get(iconFileID);

                if (icon == null) {
                    icon = add(iconFileID, path, size);
                }
            }

            if (icon != null) {
                r = icon.getBitmap();
            }
            scontext.free();
        }
        return r;
    }




    private ImageObjectAbstract get(String id) {
        for (int i = 0; i < icons.size(); i++) {
            if (id.equals(icons.get(i).toString())) {
                return icons.use(i);
            }
        }
        return null;
    }


    private ImageObjectAbstract add(String id, String path, int size) {

        ImageObjectAbstract r=null;

        if (scontext.lock()) {
            final ObjectHandle handle = scontext.getCacheService().
                    getObject(id, new SVGAssetImageObject.Factory(path, size));

            if (SVGAssetImageObject.class.isInstance(handle)) {
                final SVGAssetImageObject imageHandle = ((SVGAssetImageObject) handle);

                icons.add(imageHandle);
                r = imageHandle;
            }
            scontext.free();
        }
        return r;

    }


//    private ImageObjectAbstract add(String id) {
//
//        if (scontext.isUp()) {
//            final ObjectHandle handle = scontext.getCacheService().
//                    getObject(id, new ImageObject.Factory());
//
//            if (ImageObject.class.isInstance(handle)) {
//                final ImageObject imageHandle = ((ImageObject) handle);
//
//                icons.add(imageHandle);
//                return imageHandle;
//            }
//        }
//        return null;
//    }




    @Override
    public void close() {
        icons.close();
    }
}
