package ch.bailu.aat_lib.map.tile.source;

import org.mapsforge.core.model.Tile;

import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.service.cache.Obj;
import ch.bailu.aat_lib.service.cache.elevation.ObjTileElevationColor;
import ch.bailu.aat_lib.service.cache.elevation.ObjTileHillshade;

public class ElevationSource {

    public final static Source ELEVATION_HILLSHADE =
            new Source() {

                public static final String NAME ="Hillshade";

                public String getName() {
                    return NAME;
                }

                @Override
                public String getID(Tile t, AppContext x) {
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
                public String getID(Tile t, AppContext x) {
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
