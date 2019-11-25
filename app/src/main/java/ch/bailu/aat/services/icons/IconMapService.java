package ch.bailu.aat.services.icons;

import java.io.IOException;

import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.gpx.attributes.GpxAttributes;
import ch.bailu.aat.gpx.attributes.Keys;
import ch.bailu.aat.gpx.interfaces.GpxPointInterface;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.VirtualService;
import ch.bailu.aat.services.cache.ImageObjectAbstract;
import ch.bailu.aat.util.fs.foc.FocAsset;
import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.util_java.foc.Foc;

public final class IconMapService extends VirtualService {
    public static final float BIG_ICON_SIZE = 48;//64;
    public static final float SMALL_ICON_SIZE = 24;


    private final static int NKEY_KEY = Keys.toIndex("class");
    private final static int NKEY_VALUE = Keys.toIndex("type");

    private final static String MAP_FILE="symbols/iconmap.txt";



    private final IconMap map;
    private final IconCache cache;


    public IconMapService(ServiceContext sc) {
        super(sc);

        cache = new IconCache(sc);
        map = new IconMap();

        try {
            Foc map_file = new FocAsset(sc.getContext().getAssets(), MAP_FILE);
            new IconMapParser(map_file, map);
        } catch (IOException e) {
            AppLog.e(getContext(), this, e);
        }


    }

    public ImageObjectAbstract getIconSVG(int key, String value, int size) {
        String id = toAssetPath(key, value);

        if (id != null)
            return cache.getIcon(id, size);

        return null;
    }


    public ImageObjectAbstract getIconSVG(final GpxPointInterface point, final int size) {

        GpxAttributes attr = point.getAttributes();
        String path = toAssetPath(attr);

        return cache.getIcon(path, size);
    }


    public String toAssetPath(int key, String value) {
        IconMap.Icon icon = map.get(key, value);
        if (icon != null) return icon.svg;

        return null;
    }


    public String toAssetPath(GpxAttributes attr) {
        final IconMap.Icon icon = getIconEntry(attr);

        if (icon != null)
            return icon.svg;

        return null;
    }


    private IconMap.Icon getIconEntry(GpxAttributes attr) {
        for (int i=0; i<attr.size(); i++) {
            IconMap.Icon icon = map.get(attr.getKeyAt(i), attr.getAt(i));
            if (icon != null) return icon;
        }
        return getIconEntryNominatimType(attr);

    }


    private IconMap.Icon getIconEntryNominatimType(GpxAttributes attr) {

        if (attr.hasKey(NKEY_KEY) && attr.hasKey(NKEY_VALUE)) {
            final int key = Keys.toIndex(attr.get(NKEY_KEY));
            final String value = attr.get(NKEY_VALUE);

            return map.get(key, value);
        }
        return null;
    }



    @Override
    public void close() {
        cache.close();
    }


    @Override
    public void appendStatusText(StringBuilder builder) {
        // TODO Auto-generated method stub

    }

    public String toAssetPath(GpxPointNode gpxPointNode) {
        if (gpxPointNode.getAttributes() != null)
            return toAssetPath(gpxPointNode.getAttributes());
        return null;
    }
}
