package ch.bailu.aat.services.cache;

import org.osmdroid.tileprovider.MapTile;

import ch.bailu.aat.services.cache.CacheService.SelfOn;
import ch.bailu.aat.services.dem.DemProvider;
import ch.bailu.aat.services.dem.MultiCell;

public class NewHillshade extends ElevationTile {

    private HillshadeColorTable table;
    
    public NewHillshade(String id, SelfOn self, MapTile t) {
        super(id, self, t, splitFromZoom(t.getZoomLevel()));
    }

    
    @Override
    public void onInsert(SelfOn self) {
        table=(HillshadeColorTable) self.getObject(HillshadeColorTable.ID, new HillshadeColorTable.Factory());
        
        super.onInsert(self);
        
    }
    
    
    @Override
    public void onRemove(SelfOn self) {
        super.onRemove(self);
        table.free();
    }
    
    
    public MultiCell factoryMultiCell(DemProvider dem) {
        return MultiCell.factory(dem);
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
        private final MapTile mapTile;

        public Factory(MapTile t) {
            mapTile=t;
        }

        @Override
        public ObjectHandle factory(String id, SelfOn self) {
            
            return  new NewHillshade(id, self, mapTile);
        }
        
    } 
}
