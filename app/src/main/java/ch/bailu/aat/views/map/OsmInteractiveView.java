package ch.bailu.aat.views.map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;

import ch.bailu.aat.dispatcher.DispatcherInterface;
import ch.bailu.aat.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.preferences.SolidPositionLock;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.services.ServiceContext;

public class OsmInteractiveView extends AbsOsmView implements
        MapListener,
        OnSharedPreferenceChangeListener,
        OnTouchListener,
        OnContentUpdatedInterface {

    
    private static final float UNLOCK_TRIGGER_SIZE = 50;
    
    
    public static final String LONGITUDE_SUFFIX ="longitude";
    public static final String LATITUDE_SUFFIX ="latitude";
    private static final String ZOOM_SUFFIX ="zoom";

    
    private GeoPoint location=new GeoPoint(0,0);

    private final Storage storage;
    private final SolidPositionLock slock;


    private float motionX=0f, motionY=0f;

    private final String solidKey;
    

      
    public OsmInteractiveView(ServiceContext context, DispatcherInterface disp, String key) {
        this(
                context.getContext(),
                disp,
                key, 
                new DynTileProvider(context));
    }
    
    
    private OsmInteractiveView(Context context, DispatcherInterface dispatcher, String key, DynTileProvider p) {
        super(context, p, new MapDensity(context));

        //provider = p;

        solidKey = key;

        storage = Storage.global(context);
        slock = new SolidPositionLock(context, solidKey);
        

        map.setMapListener(this);
        map.setOnTouchListener(this);
        
        
        loadState();

        dispatcher.addTarget(this, InfoID.LOCATION);
    }


    
    private void loadState() {
        location = new GeoPoint(
                storage.readInteger(solidKey + LATITUDE_SUFFIX), 
                storage.readInteger(solidKey + LONGITUDE_SUFFIX)
                );

        MapController controller= map.getController();
        controller.setZoom(storage.readInteger(solidKey + ZOOM_SUFFIX));
        controller.setCenter(location);
    }

    



    @Override
    public void onSharedPreferenceChanged(SharedPreferences p, String key) {
        getOverlayList().onSharedPreferenceChanged(key);
        
        
        if (slock.hasKey(key)) {
            
            if (slock.isEnabled()) {
                map.getController().setCenter(location);
            }
        }
    }



    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        storage.register(this);
    }

    @Override
    public String getSolidKey() {
        return solidKey;
    }

    @Override
    public void onDetachedFromWindow() {
        saveState();
        storage.unregister(this);
        super.onDetachedFromWindow();
    }


    
    private void saveState() {
        IGeoPoint point = map.getMapCenter();

        storage.writeInteger(solidKey + LONGITUDE_SUFFIX, point.getLongitudeE6());
        storage.writeInteger(solidKey + LATITUDE_SUFFIX, point.getLatitudeE6());
        storage.writeInteger(solidKey + ZOOM_SUFFIX, map.getZoomLevel());
    }

    
    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        updateLocation(iid, info);
        refreshMap();
    }


    private void refreshMap() {
        if (slock.isEnabled()) {
            map.getController().setCenter(location);
        }
    }

    
    private void updateLocation(int iid, GpxInformation info) {
        if (iid == InfoID.LOCATION) {
            location.setLatitudeE6(info.getLatitudeE6());
            location.setLongitudeE6(info.getLongitudeE6());
        }
    }



    @Override
    public boolean onScroll(ScrollEvent event) {
        return false;
    }

    @Override
    public boolean onZoom(ZoomEvent event) {
        if (slock.isEnabled()) {
            map.getController().setCenter(location);
        }
        return false;
    }

    
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction()==MotionEvent.ACTION_DOWN) {
            motionX = event.getX();
            motionY = event.getY();
            
        } else if (event.getAction()==MotionEvent.ACTION_UP && slock.isEnabled()) {
            float size = 
                    Math.abs(motionX - event.getX()) +
                    Math.abs(motionY - event.getY());
            
            if (size > UNLOCK_TRIGGER_SIZE)
                slock.setValue(false);
        }
        return false;
    }
}
