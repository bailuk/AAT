//package ch.bailu.aat.map.tile;
//
//import android.content.Context;
//import android.graphics.drawable.Drawable;
//
//import org.mapsforge.core.graphics.TileBitmap;
//import org.mapsforge.core.model.Tile;
//
//import java.io.Closeable;
//import java.util.ArrayList;
//
//import ch.bailu.aat.services.ServiceContext;
//import ch.bailu.aat.services.cache.TileStackObject;
//
//public class TileProviderPreview extends TileProviderAbstract implements Closeable  {
//
//    private final ArrayList<TileStackObject> tiles = new ArrayList<>(10);
//
//    private final Context context;
//
//    public TileProviderPreview(ServiceContext sc) {
//        super(sc);
//        context = sc.getContext();
//    }
//
//
//    @Override
//    public Drawable getDrawable(Tile tile) {
//        final TileStackObject handle = getTileHandle(tile);
//
//        tiles.add_w(handle);
//
//        return handle.getDrawable(context.getResources());
//    }
//
//
//    @Override
//    public TileBitmap get(Tile tile) {
//        final TileStackObject handle = getTileHandle(tile);
//
//        tiles.add_w(handle);
//
//        return handle.getTileBitmap();
//    }
//
//
//    @Override
//    public boolean contains(Tile tile) {
//        return isReadyAndLoaded();
//    }
//
//    @Override
//    public int getCapacity() {
//        return tiles.pixelCount();
//    }
//
//    @Override
//    public void setCapacity(int numNeeded) {}
//
//    @Override
//    public void reDownloadTiles() {}
//
//
//    public boolean isReadyAndLoaded() {
//
//        for (int i=0; i<tiles.pixelCount(); i++) {
//            if (tiles.get(i).isReadyAndLoaded()==false) return false;
//        }
//        return true;
//    }
//
//
//    @Override
//    public void close() {
//        for (int i=0; i<tiles.pixelCount(); i++) {
//            tiles.get(i).free();
//        }
//        tiles.clear();
//    }
//
//    @Override
//    public void onAttached() {
//
//    }
//
//    @Override
//    public void onDetached() {
//
//    }
//}
//
//
