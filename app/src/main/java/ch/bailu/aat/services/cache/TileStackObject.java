package ch.bailu.aat.services.cache;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.map.android.graphics.AndroidTileBitmap;
import org.osmdroid.tileprovider.MapTile;
import org.osmdroid.views.overlay.LoadingTile;

import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.mapsforge.MapsForgeBitmap;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.background.ProcessHandle;
import ch.bailu.aat.services.cache.TileObject.Source;

public class TileStackObject extends ObjectHandle {

    public final static TileStackObject NULL=new TileStackObject();

    private final TileContainer[] tiles;
    private final SynchronizedTileBitmap bitmap=new SynchronizedTileBitmap();

    private final static Paint paint = new Paint();

    private ProcessHandle pendingUpdate=ProcessHandle.NULL;


    private final MapTile mapTile;
    private final int tileSize;

    private boolean ready=false;

    private TileStackObject() {
        super(TileStackObject.class.getSimpleName());
        tiles = new TileContainer[]{};
        mapTile = new MapTile(0, 0, 0);
        tileSize=256;
    }


    public TileStackObject(String s,  TileContainer[] t, MapTile m, int size) {
        super(s);
        tiles = t;
        mapTile=m;
        tileSize = size;

        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);


    }



    @Override
    public void onInsert(ServiceContext sc) {
        sc.getCacheService().addToBroadcaster(this);

        for (TileContainer tile: tiles) {
            tile.lock(sc);
        }

        reupdate(sc);

    }




    @Override
    public void onRemove(ServiceContext cs) {
        for (TileContainer tile: tiles) {
            tile.free();
        }
    }


    private void reupdate(ServiceContext cs) {
        ready=false;


        if (areSubtilesReady()) {
            pendingUpdate.stopLoading();
            pendingUpdate = new ReUpdate();
            cs.getBackgroundService().process(pendingUpdate);
        }
    }


    @Override
    public void onDownloaded(String id, String u, ServiceContext cs) {}


    @Override
    public void onChanged(String id, ServiceContext cs) {
        if (haveID(id)) {
            reupdate(cs);
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

    public Bitmap getBitmap() {
        return bitmap.getBitmap();
    };
    public TileBitmap getTileBitmap() {
        return bitmap.getTileBitmap();
    }

    public Drawable getDrawable(Resources res) {
        return  bitmap.getDrawable(res);
    }

    private class ReUpdate extends ProcessHandle {
        private final static int OVERLAY_ALPHA=150;
        private final static int NULL_ALPHA=255;

        public ReUpdate() {
            super();
        }

        @Override
        public long bgOnProcess() {
            MapsForgeBitmap b = bgReupdate();

            if (canContinue()) {
                bitmap.set(b);
                ready=true;
                return bitmap.getSize();
            }
            return 0;
        }



        private MapsForgeBitmap bgReupdate() {


            MapsForgeBitmap destination=null;
            Canvas canvas=null;
            int alpha=NULL_ALPHA;

            for (int i=0; i<tiles.length && canContinue(); i++) {
                TileContainer tile=tiles[i];

                Bitmap source=tile.getBitmap();

                if (alpha == NULL_ALPHA && source == null) {
                    source = LoadingTile.getBitmap();
                }

                if (source != null) {
                    if (canvas == null) {
                        //final int w = source.getWidth();
                        //final int h = source.getHeight();

                        destination = new MapsForgeBitmap(tileSize);
                        canvas = destination.getCanvas();
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
            AppBroadcaster.broadcast(context, AppBroadcaster.FILE_CHANGED_INCACHE,
                    TileStackObject.this.toString());
        }
    }


    @Override
    public long getSize() {
        return bitmap.getSize();
    }


    public void reDownload(ServiceContext sc) {
        for (TileContainer tile : tiles) {
            tile.reDownload(sc);
        }
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




        public void lock(ServiceContext sc) {
            handle = sc.getCacheService().getObject(id, factory);
        }


        public void free() {
            handle.free();
            handle = ObjectHandle.NULL;
        }

        public void reDownload(ServiceContext sc) {
            if (TileObject.class.isInstance(handle)) {
                ((TileObject)handle).reDownload(sc);
            }
        }
    }



    public static class Factory extends ObjectHandle.Factory {
        private final TileContainer[] tiles;
        private final MapTile mapTile;
        private final int size;


        public Factory(Context context, MapTile mt, int size, Source s[]) {
            mapTile = mt;
            int count=0;
            this.size = size;

            for (Source value1 : s) {
                if (isZoomLevelSupported(value1)) {
                    count++;
                }
            }

            tiles = new TileContainer[count];

            int x=0;
            for (Source value : s) {
                if (isZoomLevelSupported(value)) {
                    tiles[x] = new TileContainer(
                            value.getID(mapTile, context),
                            value.getBitmapFilter(),
                            value.getFactory(mapTile));
                    x++;
                }
            }


        }


        boolean isZoomLevelSupported(Source s) {
            final int z = mapTile.getZoomLevel();
            return (s.getMaximumZoomLevel()>=z && s.getMinimumZoomLevel()<=z);
        }
        @Override
        public ObjectHandle factory(String id, ServiceContext cs) {
            return new TileStackObject(id, tiles, mapTile, size);
        }
    }
}
