package ch.bailu.aat.services.icons;

import java.io.Closeable;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.ImageObjectAbstract;
import ch.bailu.aat.services.cache.LockCache;
import ch.bailu.aat.services.cache.ObjectHandle;
import ch.bailu.aat.services.cache.SVGAssetImageObject;

public class IconCache implements Closeable {
    private final LockCache<ImageObjectAbstract> icons = new LockCache<>(20);

    private final ServiceContext scontext;


    public IconCache(ServiceContext sc) {
        scontext = sc;
    }


    public ImageObjectAbstract getIcon(final String path, final int size) {
        final ImageObjectAbstract[] r = {null};

        if (path != null) {
            String iconFileID = SVGAssetImageObject.toID(path, size);

            ImageObjectAbstract icon = get(iconFileID);

            if (icon == null) {
                icon = add(iconFileID, path, size);
            }


            r[0] = icon;
        }
        return r[0];
    }


    private ImageObjectAbstract get(String id) {
        for (int i = 0; i < icons.size(); i++) {
            if (id.equals(icons.get(i).toString())) {
                return icons.use(i);
            }
        }
        return null;
    }


    private ImageObjectAbstract add(final String id, final String path, final int size) {
        ImageObjectAbstract r = null;

        final ObjectHandle handle = scontext.getCacheService().
                getObject(id, new SVGAssetImageObject.Factory(path, size));

        if (SVGAssetImageObject.class.isInstance(handle)) {
            final SVGAssetImageObject imageHandle = ((SVGAssetImageObject) handle);

            icons.add(imageHandle);
            r = imageHandle;
        }


        return r;
    }


    @Override
    public void close() {
        icons.close();
    }

}
