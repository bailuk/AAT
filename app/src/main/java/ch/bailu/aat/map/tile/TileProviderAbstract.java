//package ch.bailu.aat.map.tile;
//
//import org.mapsforge.core.model.Tile;
//import org.mapsforge.map.model.common.Observer;
//
//import java.util.ArrayList;
//
//import ch.bailu.aat.services.ServiceContext;
//import ch.bailu.aat.services.cache.BitmapTileObject;
//import ch.bailu.aat.services.cache.ObjectHandle;
//import ch.bailu.aat.services.cache.TileObject.Source;
//import ch.bailu.aat.services.cache.TileStackObject;
//
//public abstract class TileProviderAbstract implements TileProviderInterface {
//
//    private final ArrayList<Observer> observers = new ArrayList(2);
//    private final ServiceContext scontext;
//    private Source sources[] = new Source[]{BitmapTileObject.MAPNIK};
//    private final StringBuilder builder = new StringBuilder();
//
//    public TileProviderAbstract(ServiceContext sc) {
//        scontext = sc;
//    }
//
//    protected TileStackObject getTileHandle(Tile mapTile) {
//        if (scontext.isUp()) {
//            String id = generateTileID(mapTile);
//            ObjectHandle handle = scontext.getCacheService().getObject(
//                    id,
//                    new TileStackObject.Factory(scontext.getContext(), mapTile, sources)
//            );
//
//
//            if (TileStackObject.class.isInstance(handle)) {
//                return (TileStackObject) handle;
//            }
//        }
//
//        return TileStackObject.NULL;
//    }
//
//
//
//    private String generateTileID(Tile mapTile) {
//        builder.setLength(0);
//
//        builder.append(mapTile.zoomLevel);
//        builder.append('/');
//        builder.append(mapTile.tileX);
//        builder.append('/');
//        builder.append(mapTile.tileY);
//
//        for (Source source : sources) {
//            builder.append('/');
//            builder.append(source.getName());
//            builder.append(source.getBitmapFilter().toString());
//        }
//        return builder.toString();
//    }
//
//
//    public int getMinimumZoomLevel() {
//        return BitmapTileObject.MIN_ZOOM;
//    }
//    public int getMaximumZoomLevel() {
//        return BitmapTileObject.MAX_ZOOM;
//    }
//
//    public void setSubTileSource(Source[] s) {
//        sources=s;
//    }
//
//
//    protected void notifyChange() {
//        for (Observer o: observers) o.onChange();
//    }
//
//
//    @Override
//    public void addObserver(Observer observer) {
//        observers.add(observer);
//    }
//
//    @Override
//    public void removeObserver(Observer observer) {
//        observers.remove(observer);
//    }
//}
