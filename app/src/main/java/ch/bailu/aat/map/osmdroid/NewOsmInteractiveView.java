package ch.bailu.aat.map.osmdroid;

import android.view.MotionEvent;
import android.view.View;

import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;

import ch.bailu.aat.dispatcher.DispatcherInterface;
import ch.bailu.aat.map.layer.MapPositionLayer;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.services.ServiceContext;

public class NewOsmInteractiveView extends NewOsmView
        implements MapListener, View.OnTouchListener {


    private final Storage storage;
    private final MapPositionLayer pos;


    public NewOsmInteractiveView(ServiceContext context, DispatcherInterface disp, String key) {
        this(
                context,
                disp,
                key,
                new DynTileProvider(context));
    }


    private NewOsmInteractiveView(ServiceContext context, DispatcherInterface dispatcher, String key, DynTileProvider p) {
        super(context, p, new MapDensity(context.getContext()), key);


        storage = Storage.global(context.getContext());

        map.setMapListener(this);
        map.setOnTouchListener(this);


        pos = new MapPositionLayer(getMContext(), dispatcher);
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
