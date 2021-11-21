package ch.bailu.aat_lib.service.cache;

import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.model.Tile;

import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.map.tile.MapTileInterface;

public abstract class ObjTile extends Obj {

    private final int hash;

    public ObjTile(String id) {
        super(id);
        hash = id.hashCode();
    }

    public abstract TileBitmap getTileBitmap();
    public abstract Tile getTile();
    public abstract void reDownload(AppContext sc);

    public abstract boolean isLoaded();



    protected static long getSize(MapTileInterface bitmap, long defaultSize) {
        long size = bitmap.getSize();

        if (size == 0) size = defaultSize;

        return size;
    }


    @Override
    public int hashCode() {
        return hash;
    }


    public static final ObjTile NULL = new ObjTile("") {
        @Override
        public TileBitmap getTileBitmap() {
            return null;
        }

        @Override
        public Tile getTile() {
            return null;
        }

        @Override
        public void reDownload(AppContext sc) {}

        @Override
        public boolean isLoaded() {
            return false;
        }

        @Override
        public long getSize() {
            return MIN_SIZE;
        }

        @Override
        public void onDownloaded(String id, String url, AppContext sc) {

        }

        @Override
        public void onChanged(String id, AppContext sc) {

        }
    };
}
