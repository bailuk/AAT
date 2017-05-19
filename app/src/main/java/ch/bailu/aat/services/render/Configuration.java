package ch.bailu.aat.services.render;

import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.map.rendertheme.XmlRenderTheme;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import ch.bailu.aat.services.cache.MapsForgeTileObject;

public class Configuration {
    private final ArrayList<File> mapFiles = new ArrayList<>(10);

    private Renderer renderer;
    private String themeID;


    public TileBitmap getTile(MapsForgeTileObject o) {
        if (isConfigured() && themeID.equals(o.getThemeID()))
            return renderer.getTile(o.getTile());

        return null;
    }


    public boolean isConfigured() {
        return renderer != null;     }


    public boolean configure(File mapDir, Caches caches, XmlRenderTheme theme, String tID) {
        if (isConfigured() == false && configureMapList(mapDir)) {
            themeID = tID;
            renderer = new Renderer(theme, caches.get(themeID), mapFiles);
        }
        return isConfigured();
    }

    public void destroy() {
        if (isConfigured()) {
            renderer.destroy();
            renderer = null;
        }
    }


    private boolean configureMapList(File dir) {
        mapFiles.clear();


        dir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (name.endsWith(".map")) {
                    File file = new File(dir, name);
                    if (file.isFile()) mapFiles.add(file);
                }
                return false;
            }
        });

        return mapFiles.size() > 0;
    }
}
