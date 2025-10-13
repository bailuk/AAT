package ch.bailu.aat_lib.map.tile.source;

import org.mapsforge.core.model.Tile;
import org.mapsforge.map.rendertheme.internal.MapsforgeThemes;

import ch.bailu.aat_lib.preferences.map.SolidRenderTheme;
import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.service.cache.Obj;
import ch.bailu.aat_lib.service.cache.ObjTileMapsForge;

public class MapsForgeSource extends Source {
    public final static Source MAPSFORGE = new MapsForgeSource(MapsforgeThemes.DEFAULT.name());

    public static final String NAME = "Offline";
    private final String themeFile;
    public final String themeIdName;


    public MapsForgeSource(String xmlThemeFile) {
        themeFile = xmlThemeFile;
        themeIdName = "MF_" + SolidRenderTheme.toThemeName(xmlThemeFile);
    }

    public String getName() {
        return themeIdName;
    }

    @Override
    public String getID(Tile t, AppContext x) {
        return genID(t, themeIdName);
    }

    @Override
    public int getMinimumZoomLevel() {
        return 0;
    }

    @Override
    public int getMaximumZoomLevel() {
        return 19;
    }

    @Override
    public boolean isTransparent() {
        return false;
    }

    @Override
    public int getAlpha() {
        return OPAQUE;
    }


    @Override
    public Obj.Factory getFactory(Tile t) {
        return  new ObjTileMapsForge.Factory(t, themeFile);
    }
}
