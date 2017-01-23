//package ch.bailu.aat.services.cache;
//
//
//import android.content.Context;
//import android.graphics.Bitmap;
//
//import org.mapsforge.core.graphics.TileBitmap;
//import org.mapsforge.core.model.Tile;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.OutputStream;
//
//import ch.bailu.aat.services.ServiceContext;
//import ch.bailu.aat.services.background.FileHandle;
//import ch.bailu.aat.util.AppBroadcaster;
//import ch.bailu.aat.util.fs.FileAccess;
//
//public class HillShadeCached extends TileObject {
//
//    private final FileHandle save;
//
//    private TileObject tile = null;
//
//    private final ObjectHandle.Factory bitmapFactory, demFactory;
//    private final String bitmapID, demID;
//
//
//    private final Tile mapTile;
//
//    public HillShadeCached(String id, ServiceContext sc,  Tile t) {
//        super(id);
//
//        mapTile = t;
//
//        demID = NewHillshade.ELEVATION_HILLSHADE8.getID(t, sc.getContext());
//        demFactory = NewHillshade.ELEVATION_HILLSHADE8.getFactory(t);
//
//        bitmapID = BitmapTileObject.HILLSHADE_CACHE.getID(t, sc.getContext());
//        bitmapFactory = BitmapTileObject.HILLSHADE_CACHE.getFactory(t);
//
//
//
//        save = new FileHandle(id) {
//
//            @Override
//            public long bgOnProcess() {
//
//                OutputStream out = null;
//
//                try {
//                    out = FileAccess.openOutput(new File(bitmapID));
//
//                    Bitmap bitmap = tile.getBitmap();
//                    if (bitmap != null) {
//                        bitmap.compress(Bitmap.CompressFormat.PNG, 0, out);
//                    }
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//
//
//                } finally {
//                    FileAccess.close(out);
//                }
//                return tile.getSize();
//            }
//
//            @Override
//            public void broadcast(Context context) {
//                AppBroadcaster.broadcast(context, AppBroadcaster.FILE_CHANGED_ONDISK,
//                        bitmapID, demID);
//            }
//        };
//
//    }
//
//    @Override
//    public TileBitmap getTileBitmap() {
//        if (tile != null) return tile.getTileBitmap();
//        return null;
//    }
//
//    @Override
//    public Tile getTile() {
//        return mapTile;
//    }
//
//    @Override
//    public void onInsert(ServiceContext sc) {
//        if (isLoadable()) {
//            tile = (TileObject) sc.getCacheService().getObject(bitmapID, bitmapFactory);
//        } else {
//            tile = (TileObject) sc.getCacheService().getObject(demID, demFactory);
//        }
//        sc.getCacheService().addToBroadcaster(this);
//
//    }
//
//
//    @Override
//    public void onRemove(ServiceContext cs) {
//        tile.free();
//    }
//
//
//    @Override
//    public void reDownload(ServiceContext sc) {
//        toFile().delete();
//        tile.free();
//        tile = (TileObject) sc.getCacheService().getObject(bitmapID, bitmapFactory);
//    }
//
//
//    @Override
//    public boolean isLoaded() {
//        return tile.isLoaded();
//    }
//
//
//
//    @Override
//    public File toFile() {
//        return new File(bitmapID);
//    }
//
//
//
//    private boolean isLoadable() {
//        return toFile().exists();
//    }
//
//
//    @Override
//    public void onDownloaded(String id, String u, ServiceContext sc) {}
//
//
//    @Override
//    public void onChanged(String id, ServiceContext sc) {
//        if (id.equals(tile.toString())) {
//            AppBroadcaster.broadcast(sc.getContext(),
//                    AppBroadcaster.FILE_CHANGED_INCACHE,
//                    toString());
//
//
//            if (id.equals(demID) && tile.isLoaded() && toFile().exists()==false) {
//                sc.getBackgroundService().process(save);
//            }
//        }
//    }
//
//
//
//    @Override
//    public boolean isReady() {
//        return tile.isReady();
//    }
//
//
//    @Override
//    public long getSize() {
//        return TileObject.MIN_SIZE;
//    }
//
//    @Override
//    public Bitmap getBitmap() {
//        return tile.getBitmap();
//    }
//
//
//
//    public static class Factory extends ObjectHandle.Factory {
//        private final Tile mapTile;
//
//
//        public Factory(Tile mt) {
//            mapTile=mt;
//        }
//
//        @Override
//        public ObjectHandle factory(String id, ServiceContext cs) {
//            return new HillShadeCached(id, cs, mapTile);
//        }
//    }
//
//
//    public final static TileObject.Source ELEVATION_HILLSHADE_CACHEDX =
//            new TileObject.Source() {
//
//                @Override
//                public String getName() {
//                    return "Hillshade Cached";
//                }
//
//                @Override
//                public String getID(Tile t, Context x) {
//                    return genID(t, HillShadeCached.class.getSimpleName());
//                }
//
//                @Override
//                public int getMinimumZoomLevel() {
//                    return NewHillshade.ELEVATION_HILLSHADE8.getMinimumZoomLevel();
//                }
//
//                @Override
//                public int getMaximumZoomLevel() {
//                    return NewHillshade.ELEVATION_HILLSHADE8.getMaximumZoomLevel();
//                }
//
////                @Override
////                public boolean isTransparent() {
////                    return NewHillshade.ELEVATION_HILLSHADE8.isTransparent();
////                }
//
//                @Override
//                public int getAlpha() {
//                    return NewHillshade.ELEVATION_HILLSHADE8.getAlpha();
//                }
//
//                @Override
//                public ObjectHandle.Factory getFactory(Tile t) {
//                    return  new HillShadeCached.Factory(t);
//                }
//
//            };
//}
