package ch.bailu.aat.services.render;

import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.model.Tile;
import org.mapsforge.map.rendertheme.XmlRenderTheme;

import java.util.ArrayList;

import ch.bailu.aat.services.cache.MapsForgeTileObject;
import ch.bailu.simpleio.foc.Foc;

public class Configuration {
    private final ArrayList<Foc> mapFiles = new ArrayList<>(10);

    private Renderer renderer;
    private String themeID;


    public TileBitmap getTile(MapsForgeTileObject o) {


        if (isConfigured() && themeID.equals(o.getThemeID()))
            return renderer.getTile(o.getTile());


        return null;
    }


    public boolean isConfigured() {
        return renderer != null;     }


    public void configure(Foc mapDir, Caches caches, XmlRenderTheme theme, String tID) {
        if (isConfigured() == false && configureMapList(mapDir)) {
            themeID = tID;
            renderer = new Renderer(theme, caches.get(themeID), mapFiles);
        }
    }

    public void destroy() {
        if (isConfigured()) {
            renderer.destroy();
            renderer = null;
        }
    }


    private boolean configureMapList(Foc dir) {
        mapFiles.clear();


        dir.foreachFile(new Foc.Execute() {
            @Override
            public void execute(Foc child) {
                if (child.getName().endsWith(".map")) {
                    mapFiles.add(child);
                }
            }
        });

        return mapFiles.size() > 0;
    }

    public boolean supportsTile(Tile t) {
        return renderer.supportsTile(t);
    }
}
