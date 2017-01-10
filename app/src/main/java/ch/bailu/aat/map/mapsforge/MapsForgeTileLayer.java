package ch.bailu.aat.map.mapsforge;

import android.content.SharedPreferences;

import org.mapsforge.core.graphics.Matrix;
import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.model.Tile;
import org.mapsforge.map.layer.TileLayer;
import org.mapsforge.map.layer.queue.Job;
import org.mapsforge.map.model.MapViewPosition;
import org.mapsforge.map.model.common.Observer;

import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.map.layer.MapLayerInterface;
import ch.bailu.aat.map.tile.TileProviderDyn;
import ch.bailu.aat.services.ServiceContext;

public class MapsForgeTileLayer extends TileLayer implements MapLayerInterface, Observer {

    private  final MapsForgeTileCache cache;

    public MapsForgeTileLayer(ServiceContext sc, MapViewPosition mapViewPosition, Matrix matrix) {
        this(new MapsForgeTileCache(new TileProviderDyn(sc)), mapViewPosition, matrix);

    }

    public MapsForgeTileLayer(MapsForgeTileCache cache, MapViewPosition position, Matrix matrix) {
        super(cache, position, matrix, false, false);
        this.cache = cache;
    }

    @Override
    protected Job createJob(Tile tile) {
        return new Job(tile, false);
    }

    @Override
    protected boolean isTileStale(Tile tile, TileBitmap tileBitmap) {
        return false;
    }

    @Override
    public void onAttached() {

        cache.onAttached();
        cache.addObserver(this);
    }

    @Override
    public void onDetached() {


        cache.removeObserver(this);
        cache.onDetached();
    }




    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {

    }




    @Override
    public void drawInside(MapContext mcontext) {

    }

    @Override
    public void drawOnTop(MapContext mcontext) {

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

    @Override
    public void onChange() {
        requestRedraw();
    }
}
