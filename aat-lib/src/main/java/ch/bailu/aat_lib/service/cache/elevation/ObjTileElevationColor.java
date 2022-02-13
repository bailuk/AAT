package ch.bailu.aat_lib.service.cache.elevation;

import org.mapsforge.core.model.Tile;

import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.map.tile.MapTileInterface;
import ch.bailu.aat_lib.service.cache.Obj;
import ch.bailu.aat_lib.service.elevation.tile.DemProvider;
import ch.bailu.aat_lib.util.color.AltitudeColorTable;

public final class ObjTileElevationColor extends ObjTileElevation {

    public ObjTileElevationColor(String id, MapTileInterface b, Tile t, int _split) {
        super(id, b, t, _split);
    }

    @Override
    public void fillBuffer(int[] buffer, Raster raster, SubTile subTile, DemProvider dem) {
        final int dim = dem.getDim().DIM;
        final int bitmap_dim = subTile.pixelDim();

        int c=0;
        int old_line=-1;
        int color=0;

        for (int la = subTile.laSpan.firstPixelIndex(); la <= subTile.laSpan.lastPixelIndex(); la++) {

            final int line = raster.toLaRaster[la]*dim;
            int offset = -1;

            if (old_line != line) {


                for (int lo = subTile.loSpan.firstPixelIndex(); lo <= subTile.loSpan.lastPixelIndex(); lo++) {
                    final int new_offset=raster.toLoRaster[lo];

                    if (new_offset != offset) {
                        offset = new_offset;
                        color = AltitudeColorTable.instance().getColor(dem.getElevation(line + offset));
                    }

                    buffer[c]=color;
                    c++;
                }
            } else {
                copyLine(buffer, c-bitmap_dim, c);
                c+=bitmap_dim;
            }

            old_line=line;
        }
    }

    private void copyLine(int[] buffer, int sourceIndex, int destinationIndex) {
        final int nextLine = destinationIndex;

        for (; sourceIndex < nextLine; sourceIndex++) {
            buffer[destinationIndex] = buffer[sourceIndex];
            destinationIndex++;
        }
    }

    public static class Factory extends Obj.Factory {
        private static final int SPLIT=0;
        private final Tile mapTile;

        public Factory(Tile t) {
            mapTile=t;
        }


        @Override
        public Obj factory(String id, AppContext ac) {
            return  new ObjTileElevationColor(id, ac.createMapTile(), mapTile,SPLIT);
        }
    }
}
