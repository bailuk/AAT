//package ch.bailu.aat.map.osmdroid;
//
//import android.view.KeyEvent;
//import android.view.MotionEvent;
//
//import ch.bailu.aat.map.MapDensity;
//import ch.bailu.aat.map.tile.TileProviderInterface;
//import ch.bailu.aat.services.ServiceContext;
//
//public class OsmViewStatic extends OsmViewAbstract {
//    public OsmViewStatic(ServiceContext sc, TileProviderInterface provider, MapDensity r) {
//        super(sc, new OsmTileProvider(provider, r.getTileSize()), r, OsmViewStatic.class.getSimpleName());
//    }
//
//
//    @Override
//    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
//        return false;
//
//    }
//
//    @Override
//    public boolean onTrackballEvent(final MotionEvent event) {
//        return false;
//    }
//
//    @Override
//    public boolean dispatchTouchEvent(final MotionEvent event) {
//        return false;
//    }
//}
