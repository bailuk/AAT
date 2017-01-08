package ch.bailu.aat.services.cache;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.model.Tile;
import org.osmdroid.views.overlay.LoadingTile;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.background.ProcessHandle;
import ch.bailu.aat.services.cache.TileObject.Source;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.graphic.AppTileBitmap;
import ch.bailu.aat.util.graphic.SynchronizedBitmap;
import ch.bailu.aat.util.ui.AppLog;

public class TileStackObject extends ObjectHandle {

    public final static TileStackObject NULL=new TileStackObject();

    private final TileContainer[] tiles;
    private final SynchronizedBitmap bitmap=new SynchronizedBitmap();

    private final static Paint paint = new Paint();

    private ProcessHandle pendingUpdate=ProcessHandle.NULL;


    private final Tile mapTile;


    private boolean ready=false;

    private TileStackObject() {
        super(TileStackObject.class.getSimpleName());
        tiles = new TileContainer[]{};
        mapTile = new Tile(0,0,(byte)0,TileObject.TILE_SIZE);
    }


    public TileStackObject(String s,  TileContainer[] t, Tile m) {
        super(s);
        tiles = t;
        mapTile=m;

        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);


    }



    @Override
    public void onInsert(ServiceContext sc) {
        sc.getCacheService().addToBroadcaster(this);
        reupdate(sc);

    }




    @Override
    public void onRemove(ServiceContext cs) {
        bitmap.free();
    }


    @Override
    public void lock(ServiceContext sc) {
        super.lock(sc);
        for (TileContainer tile: tiles) {
            tile.lock(sc);
        }
    }


    public void free() {
        for (TileContainer tile: tiles) {
            tile.free();
        }
        super.free();
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
        return bitmap.getAndroidBitmap();
    }

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
            AppTileBitmap b = bgReupdate();

            if (canContinue()) {
                bitmap.set(b);
                ready=true;
                return bitmap.getSize();
            }
            return 0;
        }



        private AppTileBitmap bgReupdate() {


            AppTileBitmap destination=null;
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
                        destination = new AppTileBitmap(mapTile.tileSize);
                        destination.erase();
                        canvas = destination.getAndroidCanvas();
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

    public Tile getTile() {
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
        private final Tile mapTile;



        public Factory(Context context, Tile mt, Source s[]) {
            mapTile = mt;
            int count=0;


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
            final int z = mapTile.zoomLevel;
            return (s.getMaximumZoomLevel()>=z && s.getMinimumZoomLevel()<=z);
        }
        @Override
        public ObjectHandle factory(String id, ServiceContext cs) {
            return new TileStackObject(id, tiles, mapTile);
        }
    }



}
