package ch.bailu.aat.services.cache;

import android.content.Context;

import org.mapsforge.core.model.Tile;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.dem.DemDimension;
import ch.bailu.aat.services.dem.DemGeoToIndex;
import ch.bailu.aat.services.dem.DemProvider;
import ch.bailu.aat.services.dem.DemSplitter;
import ch.bailu.aat.services.dem.MultiCell;
import ch.bailu.aat.services.dem.MultiCell8;

public class NewHillshade extends ElevationTile {

    private HillshadeColorTable table;

    public NewHillshade(String id, ServiceContext sc, Tile t) {
        super(id, sc, t, splitFromZoom(t.zoomLevel));
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
    public void fillBitmap(int[] bitmap, int[] toLaRaster, int[] toLoRaster,
                           Span laSpan, Span loSpan, DemProvider demtile) {
        final int demtile_dim = demtile.getDim().DIM;
        final int bitmap_dim = loSpan.size();

        int color=0;
        int index=0;
        int old_line=-1;

        final MultiCell mcell = factoryMultiCell(demtile);

        for (int la=laSpan.start(); la< laSpan.end(); la++) {
            final int line = toLaRaster[la]*demtile_dim;

            if (old_line != line) {
                int old_offset = -1;

                for (int lo=loSpan.start(); lo<loSpan.end(); lo++) {
                    final int offset=toLoRaster[lo];

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




    public static int splitFromZoom(int zoom) {
        int split = 0;
        if (zoom > 11) {
            split++;
        }
/*        
        if (zoom > 13) {
            split++;
        }
  */
        return split;
    }


    public static class Factory extends ObjectHandle.Factory {
        private final Tile mapTile;

        public Factory(Tile t) {
            mapTile=t;
        }

        @Override
        public ObjectHandle factory(String id, ServiceContext sc) {
            return  new NewHillshade(id, sc, mapTile);
        }

    }


    public final static Source ELEVATION_HILLSHADE8 =
            new Source() {

                @Override
                public String getName() {
                    return "Hillshade 8*";
                }

                @Override
                public String getID(Tile t, Context x) {
                    return genID(t, NewHillshade.class.getSimpleName());
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
                public ObjectHandle.Factory getFactory(Tile mt) {
                    return  new NewHillshade.Factory(mt);
                }

                @Override
                public TileBitmapFilter getBitmapFilter() {
                    return TileBitmapFilter.COPY_FILTER;
                }
            };
}
