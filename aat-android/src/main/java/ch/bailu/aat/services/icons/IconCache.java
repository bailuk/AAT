package ch.bailu.aat.services.icons;

import java.io.Closeable;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.LockCache;
import ch.bailu.aat.services.cache.Obj;
import ch.bailu.aat.services.cache.ObjImageAbstract;
import ch.bailu.aat.services.cache.ObjSVGAsset;
import ch.bailu.aat_lib.util.WithStatusText;
import ch.bailu.util.Objects;

public final class IconCache implements Closeable, WithStatusText {
    private final LockCache<ObjImageAbstract> icons = new LockCache<>(20);

    private final ServiceContext scontext;


    public IconCache(ServiceContext sc) {
        scontext = sc;
    }


    public ObjImageAbstract getIcon(final String path, final int size) {
        final ObjImageAbstract[] r = {null};

        if (path != null) {
            String iconFileID = ObjSVGAsset.toID(path, size);

            ObjImageAbstract icon = get(iconFileID);

            if (icon == null) {
                icon = add(iconFileID, path, size);
            }


            r[0] = icon;
        }
        return r[0];
    }


    private ObjImageAbstract get(String id) {
        for (int i = 0; i < icons.size(); i++) {
            if (Objects.equals(id, icons.get(i).toString())) {
                return icons.use(i);
            }
        }
        return null;
    }


    private ObjImageAbstract add(final String id, final String path, final int size) {
        ObjImageAbstract r = null;

        final Obj handle = scontext.getCacheService().
                getObject(id, new ObjSVGAsset.Factory(path, size));

        if (ObjSVGAsset.class.isInstance(handle)) {
            final ObjSVGAsset imageHandle = ((ObjSVGAsset) handle);

            icons.add(imageHandle);
            r = imageHandle;
        }


        return r;
    }


    @Override
    public void close() {
        icons.close();
    }

    @Override
    public void appendStatusText(StringBuilder builder) {
        builder.append("IconCache (icons) size: ").append(icons.size()).append("<br>");
    }
}
