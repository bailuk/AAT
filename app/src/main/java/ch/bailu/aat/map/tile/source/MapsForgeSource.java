package ch.bailu.aat.map.tile.source;

import android.content.Context;

import org.mapsforge.core.model.Tile;
import org.mapsforge.map.rendertheme.InternalRenderTheme;

import ch.bailu.aat.preferences.map.SolidRenderTheme;
import ch.bailu.aat.services.cache.MapsForgeTileObject;
import ch.bailu.aat.services.cache.ObjectHandle;

public class MapsForgeSource extends Source {
    public final static Source MAPSFORGE = new MapsForgeSource(InternalRenderTheme.DEFAULT.name());

    public static final String NAME = "MapsForge";
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
    public String getID(Tile t, Context x) {
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
    public ObjectHandle.Factory getFactory(Tile t) {
        return  new MapsForgeTileObject.Factory(t, themeFile);
    }
}
