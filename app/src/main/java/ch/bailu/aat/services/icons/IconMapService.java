package ch.bailu.aat.services.icons;

import java.io.File;
import java.io.IOException;

import ch.bailu.aat.gpx.GpxAttributes;
import ch.bailu.aat.gpx.interfaces.GpxPointInterface;
import ch.bailu.aat.util.fs.AppDirectory;
import ch.bailu.aat.util.graphic.AppBitmap;
import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.aat.util.fs.FileAccess;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.VirtualService;

public class IconMapService extends VirtualService {
    private final String NKEY_KEY = "class";
    private final String NKEY_VALUE = "type";

    private final static String MAP_FILE="iconmap.txt";


    private final IconMap map;
    private final IconCache cache;


    public IconMapService(ServiceContext sc) {
        super(sc);

        cache = new IconCache(sc);

        File directory = AppDirectory.getDataDirectory(getContext(), AppDirectory.DIR_OSM_FEATURES_ICONS);
        map = new IconMap(directory.toString());

        final File mapFile = new File(directory, MAP_FILE);

        if (mapFile.exists()) {
            try {
                new IconMapParser(new FileAccess(mapFile), map);
            } catch (IOException e) {
                AppLog.e(getContext(), this, e);
            }
        }


    }


    public AppBitmap getIconSVG(GpxPointInterface point, int size) {
        return cache.getIcon(point, size);
    }

    public String getSVGIconPath(GpxAttributes attr) {
        final IconMap.Icon icon = getIconEntry(attr);

        if (icon != null) {

            return icon.svg;
        }
        return null;
    }


    /*
    public AppBitmap getIcon(GpxPointInterface point) {
        return cache.getIcon(point);
    }
*/

    private IconMap.Icon getIconEntry(GpxAttributes attr) {
        for (int i=0; i<attr.size(); i++) {
            IconMap.Icon icon = map.get(attr.getKey(i), attr.getValue(i));
            if (icon != null) return icon;
        }
        return getIconEntryNominatimType(attr);

    }


    private IconMap.Icon getIconEntryNominatimType(GpxAttributes attr) {
        String key = attr.get(NKEY_KEY);

        if (key != null) {
            String value = attr.get(NKEY_VALUE);
            if (value != null)
                return map.get(key, value);
        }
        return null;
    }

  /*
    public String getIconPath(GpxAttributes attr) {
        IconMap.Icon i= getIconEntry(attr);

        if (i != null) return i.big;
        return null;
    }


*/
    private String getBigIconPath(String key, String value) {
        final IconMap.Icon icon = map.get(key,value);

        if (icon == null) {
            return null;
        }

        return icon.big;
    }



    public void iconify(StringBuilder html, String key, String value) {
        String icon = getBigIconPath(key, value);

        if (icon != null) {
            html.append("<p><img src=\"");
            html.append(icon);
            html.append("\"/></p>");
        }
    }



    @Override
    public void close() {
        cache.close();
    }


    @Override
    public void appendStatusText(StringBuilder builder) {
        // TODO Auto-generated method stub

    }

}
