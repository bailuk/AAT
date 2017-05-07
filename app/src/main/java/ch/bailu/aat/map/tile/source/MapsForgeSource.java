package ch.bailu.aat.map.tile.source;

import android.content.Context;

import org.mapsforge.core.model.Tile;
import org.mapsforge.map.rendertheme.InternalRenderTheme;

import java.io.File;

import ch.bailu.aat.services.cache.MapsForgeTileObject;
import ch.bailu.aat.services.cache.ObjectHandle;

public class MapsForgeSource extends Source {
    public final static Source MAPSFORGE = new MapsForgeSource(InternalRenderTheme.DEFAULT.name());

    public static final String NAME = "MapsForge";
    private final String themeFile;
    private final String idName;


    public MapsForgeSource(String xmlThemeFileName) {
        themeFile = xmlThemeFileName;
        idName = "MF_" + new File(themeFile).getName().replace(".xml", "");
    }

    public String getName() {
        return NAME;
    }

    @Override
    public String getID(Tile t, Context x) {
        return genID(t, idName);
    }

    @Override
    public int getMinimumZoomLevel() {
        return 0;
    }

    @Override
    public int getMaximumZoomLevel() {
        return 18;
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
    public int getPaintFlags() {
        return 0;
    }

    @Override
    public ObjectHandle.Factory getFactory(Tile t) {
        return  new MapsForgeTileObject.Factory(t, themeFile);
    }
}
