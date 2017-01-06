package ch.bailu.aat.map.osmdroid;

import android.view.MotionEvent;
import android.view.View;

import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;

import ch.bailu.aat.dispatcher.DispatcherInterface;
import ch.bailu.aat.map.MapDensity;
import ch.bailu.aat.map.layer.MapPositionLayer;
import ch.bailu.aat.map.tile.TileProviderDyn;
import ch.bailu.aat.map.tile.TileProviderInterface;
import ch.bailu.aat.preferences.SolidTileSize;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.services.ServiceContext;

public class OsmViewInteractive extends OsmViewAbstract
        implements MapListener, View.OnTouchListener {


    private final Storage storage;
    private final MapPositionLayer pos;


    public OsmViewInteractive(ServiceContext sc, DispatcherInterface disp, String key) {
        this(
                sc,
                disp,
                key,
                new TileProviderDyn(sc),
                new MapDensity(sc.getContext()));
    }


    private OsmViewInteractive(ServiceContext sc,
                               DispatcherInterface d,
                               String key,
                               TileProviderInterface p,
                               MapDensity res) {
        super(sc,
                new OsmTileProvider(p, res.getTileSize()),
                res, key);


        storage = Storage.global(sc.getContext());

        map.setMapListener(this);
        map.setOnTouchListener(this);


        pos = new MapPositionLayer(getMContext(), d);
        add(pos);
    }


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        storage.register(this);
    }


    @Override
    public void onDetachedFromWindow() {
        storage.unregister(this);
        super.onDetachedFromWindow();
    }


    @Override
    public boolean onScroll(ScrollEvent event) {
        return false;
    }

    @Override
    public boolean onZoom(ZoomEvent event) {
        return false;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        pos.onTouch(event);
        return false;
    }

}
