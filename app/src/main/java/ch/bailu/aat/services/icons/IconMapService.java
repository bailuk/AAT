package ch.bailu.aat.services.icons;

import android.graphics.Bitmap;

import java.io.File;
import java.io.IOException;

import ch.bailu.aat.gpx.GpxAttributes;
import ch.bailu.aat.gpx.interfaces.GpxPointInterface;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.VirtualService;
import ch.bailu.aat.util.fs.AbsAccess;
import ch.bailu.aat.util.fs.AppDirectory;
import ch.bailu.aat.util.fs.AssetAccess;
import ch.bailu.aat.util.fs.FileAccess;
import ch.bailu.aat.util.ui.AppLog;

public class IconMapService extends VirtualService {
    private final String NKEY_KEY = "class";
    private final String NKEY_VALUE = "type";

    private final static String MAP_FILE="symbols/iconmap.txt";


    private final IconMap map;
    private final IconCache cache;


    public IconMapService(ServiceContext sc) {
        super(sc);

        cache = new IconCache(sc);
        map = new IconMap();

        try {
            AbsAccess map_file = new AssetAccess(sc.getContext().getAssets(), MAP_FILE);
            new IconMapParser(map_file, map);
        } catch (IOException e) {
            AppLog.e(getContext(), this, e);
        }


    }


    public Bitmap getIconSVG(GpxPointInterface point, int size) {
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



    public void iconify(StringBuilder html, String key, String value) {
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
