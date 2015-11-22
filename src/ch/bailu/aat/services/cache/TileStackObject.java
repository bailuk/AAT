package ch.bailu.aat.services.cache;

import org.osmdroid.tileprovider.MapTile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.services.background.ProcessHandle;
import ch.bailu.aat.services.cache.CacheService.SelfOn;
import ch.bailu.aat.services.cache.TileObject.Source;

public class TileStackObject extends ObjectHandle {

    public final static TileStackObject NULL=new TileStackObject();

    private final TileContainer[] tiles;
    private final SynchronizedBitmap bitmap=new SynchronizedBitmap();
    private final static Paint paint = new Paint();

    private ProcessHandle pendingUpdate=ProcessHandle.NULL;


    private final MapTile mapTile;

    private boolean ready=false;
    
    private TileStackObject() {
        super(TileStackObject.class.getSimpleName());
        tiles = new TileContainer[]{};
        mapTile = new MapTile(0, 0, 0);
    }


    public TileStackObject(String s, SelfOn self,  TileContainer[] t, MapTile m) {
        super(s);
        tiles = t;
        mapTile=m;

        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);

        
    }



    @Override
    public void onInsert(SelfOn self) {
        self.broadcaster.put(this);
        
        for (TileContainer tile: tiles) {
            tile.lock(self);
        }
        
        reupdate(self);
        
    }




    @Override
    public void onRemove(SelfOn self) {
        for (TileContainer tile: tiles) {
            tile.free();
        }
    }


    private void reupdate(SelfOn self) {
        ready=false;
        
        
        if (areSubtilesReady()) {
            pendingUpdate.stopLoading();
            pendingUpdate = new ReUpdate();
            self.background.process(pendingUpdate);
        }
    }


    @Override
    public void onDownloaded(String id, String u, SelfOn self) {}


    @Override
    public void onChanged(String id, SelfOn self) {
        if (haveID(id)) {
            reupdate(self);
        }
    }


    private boolean haveID(String id) {
        for (TileContainer tile: tiles) {
            if (tile.id.equals(id)) return true;
        }
        return false;
    }


    /**
     * Sub-tiles are either loaded _and_ painted or not yet downloaded 
     */
    @Override
    public boolean isReady() {
        return ready;
    }


    private boolean areSubtilesReady() {
        for (TileContainer tile: tiles) {
            if (tile.handle.isReady() == false) return false;
        }

        return true;
    }

    public Drawable getDrawable() {
        Drawable r =  bitmap.getDrawable();
        return r;
    }

    private class ReUpdate extends ProcessHandle {

        @Override
        public long bgOnProcess() {
            Bitmap b = bgReupdate();
            
            if (canContinue()) {
                bitmap.set(b);
                ready=true;
                return bitmap.getSize();
            }
            return 0;
        }


        private Bitmap bgReupdate() {
            final int OVERLAY_ALPHA=180;

            Bitmap destination=null;
            Bitmap source=null;
            Canvas canvas=null;
            int alpha=255;

            for (int i=0; i<tiles.length && canContinue(); i++) {
                TileContainer tile=tiles[i];

                source=tile.getBitmap();

                if (source != null) {

                    if (canvas == null) {
                        final int w = source.getWidth();
                        final int h = source.getHeight();

                        destination = SynchronizedBitmap.createBitmap(w, h);
                        canvas = new Canvas(destination);
                    }
                    tile.filter.applayFilter(canvas, source, alpha);
                    alpha=OVERLAY_ALPHA;
                }           
            }
            return destination;
        }

        @Override
        public String toString() {
            return TileStackObject.this.toString();
        }



        @Override
        public void broadcast(Context context) {
            AppBroadcaster.broadcast(context, AppBroadcaster.FILE_CHANGED_INCACHE, TileStackObject.this.toString());

        }
    }


    @Override
    public long getSize() {
        return bitmap.getSize();
    }


    public void deleteFromDisk() {
        // TODO Auto-generated method stub

    }

    public MapTile getTile() {
        return mapTile;
    }

    public boolean isInStack(String id) {
        for (TileContainer tile: tiles) {
            if (tile.id.equals(id)) return true;
        }
        return false;
    }    




    public static class TileContainer {
        private final String id;
        private final ObjectHandle.Factory factory;
        private final TileBitmapFilter filter;


        private ObjectHandle handle = ObjectHandle.NULL;

        public TileContainer(String i, TileBitmapFilter fil, ObjectHandle.Factory f) {
            id=i;
            factory = f;
            filter = fil;
        }


        public Bitmap getBitmap() {
            if (TileObject.class.isInstance(handle)) {
                return ((TileObject)handle).getBitmap();
            }
            return null;
        }




        public void lock(SelfOn self) {
            handle = self.getObject(id, factory);
        }


        public void free() {
            handle.free();
        }
    }



    public static class Factory extends ObjectHandle.Factory {
        private final TileContainer[] tiles;
        private final MapTile mapTile;



        public Factory(Context context, MapTile mt, Source s[], TileBitmapFilter[] filters) {
            mapTile = mt;
            int count=0;

            for (int i=0; i<s.length; i++) {
                if (isZoomLevelSupported(s[i])) {
                    count++;
                }
            }

            tiles = new TileContainer[count];

            int x=0;
            for (int i=0; i<s.length; i++) {
                if (isZoomLevelSupported(s[i])) {
                    tiles[x]=new TileContainer(
                            s[i].getID(mapTile, context),
                            filters[i],
                            s[i].getFactory(mapTile));
                    x++;
                }
            }


        }


        boolean isZoomLevelSupported(Source s) {
            final int z = mapTile.getZoomLevel();
            return (s.getMaximumZoomLevel()>=z && s.getMinimumZoomLevel()<=z);
        }
        @Override
        public ObjectHandle factory(String id, SelfOn self) {
            return new TileStackObject(id, self, tiles, mapTile);
        }
    }
}
