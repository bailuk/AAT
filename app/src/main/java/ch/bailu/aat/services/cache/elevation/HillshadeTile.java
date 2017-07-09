package ch.bailu.aat.services.cache.elevation;

import org.mapsforge.core.model.Tile;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.ObjectHandle;
import ch.bailu.aat.services.dem.tile.DemDimension;
import ch.bailu.aat.services.dem.tile.DemGeoToIndex;
import ch.bailu.aat.services.dem.tile.DemProvider;
import ch.bailu.aat.services.dem.tile.DemSplitter;
import ch.bailu.aat.services.dem.tile.MultiCell;
import ch.bailu.aat.services.dem.tile.MultiCell8;

public class HillshadeTile extends ElevationTile {

    private HillshadeColorTable table;

    public HillshadeTile(String id, Tile t) {
        super(id, t, splitFromZoom(t.zoomLevel));
    }


    @Override
    public void onInsert(ServiceContext sc) {
        table=(HillshadeColorTable) sc.getCacheService().getObject(HillshadeColorTable.ID, new HillshadeColorTable.Factory());

        super.onInsert(sc);

    }


    @Override
    public void onRemove(ServiceContext sc) {
        super.onRemove(sc);
        table.free();
    }

    @Override
    public long getSize() {
        return getBytesHack(TILE_SIZE);
    }

    @Override
    public DemGeoToIndex factoryGeoToIndex(DemDimension dim) {
        return new DemGeoToIndex(dim, true);
    }


    @Override
    public DemProvider factorySplitter(DemProvider dem) {
        return new DemSplitter(dem);
    }
    public MultiCell factoryMultiCell(DemProvider dem) {
        return new MultiCell8(dem);
    }



    @Override
    public void fillBuffer(int[] bitmap, Raster raster, SubTile subTile, DemProvider demtile) {
        final int demtile_dim = demtile.getDim().DIM;
        final int bitmap_dim = subTile.pixelDim();

        int color=0;
        int index=0;
        int old_line=-1;

        final MultiCell mcell = factoryMultiCell(demtile);

        for (int la = subTile.laSpan.firstPixelIndex(); la< subTile.laSpan.lastPixelIndex(); la++) {
            final int line = raster.toLaRaster[la]*demtile_dim;

            if (old_line != line) {
                int old_offset = -1;

                for (int lo = subTile.loSpan.firstPixelIndex(); lo<subTile.loSpan.lastPixelIndex(); lo++) {
                    final int offset=raster.toLoRaster[lo];

                    if (old_offset != offset) {
                        old_offset = offset;

                        mcell.set(line+offset);
                        color = table.getColor(mcell);
                    }

                    bitmap[index]=color;
                    index++;
                }
            } else {
                copyLine(bitmap, index-bitmap_dim, index);
                index+=bitmap_dim;
            }

            old_line=line;
        }
    }

    private void copyLine(int[] buffer, int cs, int cd) {
        final int next_line=cd;

        for (; cs < next_line; cs++) {
            buffer[cd]=buffer[cs];
            cd++;
        }
    }



    private static int splitFromZoom(int zoom) {
        int split = 0;
        if (zoom > 11) {
            split++;
        }

        return split;
    }


    public static class Factory extends ObjectHandle.Factory {
        private final Tile mapTile;

        public Factory(Tile t) {
            mapTile=t;
        }

        @Override
        public ObjectHandle factory(String id, ServiceContext sc) {
            return  new HillshadeTile(id, mapTile);
        }

    }
}
