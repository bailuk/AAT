package ch.bailu.aat.services.cache;

import android.content.Context;
import android.graphics.Bitmap;

import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.model.Tile;

import java.io.IOException;
import java.io.OutputStream;

import ch.bailu.aat.map.tile.source.Source;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.background.FileHandle;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.fs.AppDirectory;
import ch.bailu.aat.util.fs.foc.FocAndroid;
import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.simpleio.foc.Foc;

public class CachedTileObject extends TileObject {
    private final static int MIN_SAVE_ZOOM_LEVEL = 16;

    private final Tile mapTile;

    private final ObjectHandle.Factory cachedFactory, sourceFactory;
    private final String cachedID, sourceID;

    private TileObject tile = null;

    private final FileHandle save;


    public CachedTileObject(String id, final ServiceContext sc,  Tile t, Source source) {
        super(id);
        mapTile = t;

        sourceID = source.getID(t, sc.getContext());
        sourceFactory = source.getFactory(t);

        final Source cached = new CachedSource(source);

        cachedID = cached.getID(t, sc.getContext());
        cachedFactory = cached.getFactory(t);

        save = new FileHandle(id) {

            @Override
            public long bgOnProcess() {
                long size = MIN_SIZE;

                if (!toFile(sc.getContext()).exists()) {
                    OutputStream out = null;
                    ObjectHandle handle = sc.getCacheService().getObject(sourceID);

                    try {
                        if (handle instanceof TileObject) {
                            TileObject tileObject = (TileObject) handle;

                            out = toFile(sc.getContext()).openW();

                            Bitmap bitmap = tileObject.getBitmap();
                            if (bitmap != null) {
                                bitmap.compress(Bitmap.CompressFormat.PNG, 0, out);
                            }
                        }
                    } catch (IOException e) {
                        AppLog.d(this, "Save bitmap error! Delete file here?");


                    } finally {
                        size = handle.getSize();
                        Foc.close(out);
                        handle.free();
                    }
                }
                return size;
            }

            @Override
            public void broadcast(Context context) {
                AppBroadcaster.broadcast(context, AppBroadcaster.FILE_CHANGED_ONDISK,
                        cachedID, sourceID);
            }
        };
    }

    @Override
    public void onInsert(ServiceContext sc) {
        if (isLoadable(sc.getContext())) {
            tile = (TileObject) sc.getCacheService().getObject(cachedID, cachedFactory);
        } else {
            tile = (TileObject) sc.getCacheService().getObject(sourceID, sourceFactory);
        }
        sc.getCacheService().addToBroadcaster(this);
    }

    private boolean isLoadable(Context c) {
        return toFile(c).exists();
    }

    @Override
    public Foc toFile(Context c) {
        return FocAndroid.factory(c, cachedID);
    }


    @Override
    public void onChanged(String id, ServiceContext sc) {
        if (id.equals(tile.toString())) {
            AppBroadcaster.broadcast(sc.getContext(),
                    AppBroadcaster.FILE_CHANGED_INCACHE,
                    toString());



            if (
                    mapTile.zoomLevel <= MIN_SAVE_ZOOM_LEVEL &&
                    id.equals(sourceID) &&
                            tile.isLoaded()) {

                sc.getBackgroundService().process(save);
            }
        }
    }


    @Override
    public void onRemove(ServiceContext cs) {
        tile.free();
    }


    @Override
    public Bitmap getBitmap() {
        if (tile != null) return tile.getBitmap();
        return null;
    }

    @Override
    public TileBitmap getTileBitmap() {
        if (tile != null) return tile.getTileBitmap();
        return null;
    }

    @Override
    public Tile getTile() {
        return mapTile;
    }

    @Override
    public void reDownload(ServiceContext sc) {
        toFile(sc.getContext()).rm();

        tile.free();
        tile = (TileObject) sc.getCacheService().getObject(sourceID, sourceFactory);
    }

    @Override
    public boolean isLoaded() {
        return (tile != null && tile.isLoaded());
    }

    @Override
    public long getSize() {
        if (tile != null) return tile.getSize();
        return MIN_SIZE;
    }

    @Override
    public void onDownloaded(String id, String url, ServiceContext sc) {}

    @Override
    public synchronized void access() {
        super.access();
        makeOld();
    }



    public static class Factory extends ObjectHandle.Factory {
        private final Source source;
        private final Tile tile;

        public Factory(Tile t, Source s) {
            source = s;
            tile = t;
        }

        @Override
        public ObjectHandle factory(String id, ServiceContext cs) {
            return new CachedTileObject(id, cs, tile, source);
        }
    }

    private static class CachedSource extends Source {
        public final static String EXT = ".png";

        private final Source generated;

        public CachedSource(Source gen) {
            generated = gen;
        }

        public String getName() {
            return generated.getName();
        }

        @Override
        public String getID(Tile tile, Context context) {
            final String relativePath = generated.getID(tile, context) + EXT;
            return AppDirectory.getTileFile(relativePath, context).toString();
        }


        @Override
        public int getMinimumZoomLevel() {
            return generated.getMinimumZoomLevel();
        }

        @Override
        public int getMaximumZoomLevel() {
            return generated.getMaximumZoomLevel();
        }

        @Override
        public int getAlpha() {
            return generated.getAlpha();
        }

        @Override
        public int getPaintFlags() {
            return generated.getPaintFlags();
        }

        @Override
        public TileObject.Factory getFactory(Tile tile) {
            return new LoadableBitmapTileObject.Factory(tile, generated.isTransparent());
        }

        @Override
        public boolean isTransparent() {
            return generated.isTransparent();
        }
    }
}
