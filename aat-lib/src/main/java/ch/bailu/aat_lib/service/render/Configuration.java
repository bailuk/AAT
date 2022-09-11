package ch.bailu.aat_lib.service.render;

import org.mapsforge.core.model.Tile;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.rendertheme.XmlRenderTheme;

import java.util.ArrayList;

import ch.bailu.aat_lib.logger.AppLog;
import ch.bailu.aat_lib.preferences.map.SolidMapsForgeDirectory;
import ch.bailu.aat_lib.resources.Res;
import ch.bailu.aat_lib.service.cache.ObjTileMapsForge;
import ch.bailu.foc.Foc;

public final class Configuration {
    private final ArrayList<Foc> mapFiles = new ArrayList<>();

    private Renderer renderer;
    private String themeID;

    public boolean isConfigured() {
        return renderer != null;     }

    public void configure(Foc mapDir, TileCache cache, XmlRenderTheme theme, String tID) {
        if (!isConfigured() && configureMapList(mapDir)) {
            themeID = tID;

            try {
                renderer = new Renderer(theme, cache, mapFiles);
            } catch (Exception e) {
                AppLog.e(renderer, e);
                renderer = null;
            }
        } else {
            AppLog.e(this, Res.str().error_no_map_file() + mapDir.getPath());
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

        if (dir.isFile()) {
            mapFiles.add(dir);
        } else {
            dir.foreachFile(child -> {
                if (child.getName().endsWith(SolidMapsForgeDirectory.EXTENSION)) {
                    mapFiles.add(child);
                }
            });
        }

        return mapFiles.size() > 0;
    }

    public boolean supportsTile(Tile t) {
        return isConfigured() && renderer.supportsTile(t);
    }

    public void lockToRenderer(ObjTileMapsForge o) {
        if (isConfigured() && themeID.equals(o.getThemeID())) {
            renderer.addJob(o.getTile());
        }
    }
}
