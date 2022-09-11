package ch.bailu.aat_lib.service.icons;

import java.io.IOException;

import ch.bailu.aat_lib.gpx.GpxPointNode;
import ch.bailu.aat_lib.gpx.attributes.GpxAttributes;
import ch.bailu.aat_lib.gpx.attributes.Keys;
import ch.bailu.aat_lib.gpx.interfaces.GpxPointInterface;
import ch.bailu.aat_lib.logger.AppLog;
import ch.bailu.aat_lib.service.IconMapServiceInterface;
import ch.bailu.aat_lib.service.ServicesInterface;
import ch.bailu.aat_lib.service.VirtualService;
import ch.bailu.aat_lib.service.cache.icons.ObjImageAbstract;
import ch.bailu.aat_lib.util.WithStatusText;
import ch.bailu.foc.Foc;
import ch.bailu.foc.FocFactory;

public final class IconMapService extends VirtualService implements WithStatusText, IconMapServiceInterface {
    public static final float BIG_ICON_SIZE = 48;

    private final static int NKEY_KEY = Keys.toIndex("class");
    private final static int NKEY_VALUE = Keys.toIndex("type");

    public final static String SVG_SUFFIX = ".svg";
    public final static String SVG_DIRECTORY = "icons/";
    private final static String SVG_MAP_FILE = SVG_DIRECTORY + "iconmap.txt";


    private final IconMap map;
    private final IconCache cache;


    public IconMapService(ServicesInterface sc, FocFactory focFactory) {

        cache = new IconCache(sc);
        map = new IconMap();

        try {
            Foc map_file = focFactory.toFoc(SVG_MAP_FILE);
            new IconMapParser(map_file, map);
        } catch (IOException e) {
            AppLog.e(this, e);
        }
    }


    public ObjImageAbstract getIconSVG(final GpxPointInterface point, final int size) {
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

    public void close() {
        cache.close();
    }

    @Override
    public void appendStatusText(StringBuilder builder) {
        map.appendStatusText(builder);
        cache.appendStatusText(builder);
    }

    public String toAssetPath(GpxPointNode gpxPointNode) {
        if (gpxPointNode.getAttributes() != null)
            return toAssetPath(gpxPointNode.getAttributes());
        return null;
    }
}
