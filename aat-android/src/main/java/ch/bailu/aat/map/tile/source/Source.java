package ch.bailu.aat.map.tile.source;

import android.content.Context;

import org.mapsforge.core.model.Tile;

import ch.bailu.aat_lib.service.cache.Obj;
import ch.bailu.aat.services.cache.elevation.ObjTileElevationColor;
import ch.bailu.aat.services.cache.elevation.ObjTileHillshade;

public abstract class Source {
    public final static String EXT = ".png";

    public final static int TRANSPARENT = 150;
    public final static int OPAQUE = 255;

    public abstract String getName();
    public abstract String getID(Tile aTile, Context context);

    public abstract int getMinimumZoomLevel();
    public abstract int getMaximumZoomLevel();

    public abstract boolean isTransparent();
    public abstract int getAlpha();
    public abstract Obj.Factory getFactory(Tile tile);


    public int getPaintFlags() {return 0;}

    public static String genRelativeFilePath(final Tile tile, String name) {
        return  genID(tile,name) +  EXT;

    }

    public static String genID(Tile t, String name) {
        return name + "/" + t.zoomLevel + "/" + t.tileX + "/" + t.tileY;

    }





    public final static Source ELEVATION_HILLSHADE =
            new Source() {

                public static final String NAME ="Hillshade";

                public String getName() {
                    return NAME;
                }

                @Override
                public String getID(Tile t, Context x) {
                    return genID(t, NAME);
                }

                @Override
                public int getMinimumZoomLevel() {
                    return 8;
                }

                @Override
                public int getMaximumZoomLevel() {
                    return 14;
                }

                @Override
                public boolean isTransparent() {
                    return true;
                }


                @Override
                public int getAlpha() {
                    return OPAQUE;
                }

                @Override
                public Obj.Factory getFactory(Tile mt) {
                    return  new ObjTileHillshade.Factory(mt);
                }

            };

    public final static Source ELEVATION_COLOR =
            new Source() {

                public String getName() {
                    return "ElevationColor";
                }

                @Override
                public String getID(Tile t, Context x) {
                    return Source.genID(t, ObjTileElevationColor.class.getSimpleName());
                }

                @Override
                public int getMinimumZoomLevel() {
                    return ELEVATION_HILLSHADE.getMinimumZoomLevel();
                }

                @Override
                public int getMaximumZoomLevel() {
                    return ELEVATION_HILLSHADE.getMaximumZoomLevel();
                }

                @Override
                public boolean isTransparent() {
                    return false;
                }

                @Override
                public int getAlpha() {
                    return 50;
                }


                @Override
                public Obj.Factory getFactory(Tile mt) {
                    return  new ObjTileElevationColor.Factory(mt);
                }

            };

}

