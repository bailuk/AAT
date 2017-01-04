package ch.bailu.aat.map.osmdroid.overlay.control;

import android.util.SparseArray;
import android.view.View;

import org.osmdroid.views.MapView;

import ch.bailu.aat.R;
import ch.bailu.aat.dispatcher.DispatcherInterface;
import ch.bailu.aat.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.aat.util.ui.ToolTip;
import ch.bailu.aat.preferences.SolidPositionLock;
import ch.bailu.aat.views.ControlBar;
import ch.bailu.aat.map.osmdroid.AbsOsmView;
import ch.bailu.aat.map.osmdroid.OsmInteractiveView;


public class NavigationBarOverlay extends ControlBarOverlay implements OnContentUpdatedInterface {
    private final View buttonPlus;
    private final View buttonMinus;
    private final View buttonFrame;

    private final SparseArray<GpxInformation> infoCache = new SparseArray<>(10);

    private int boundingCycle=0;


    public NavigationBarOverlay(OsmInteractiveView o, DispatcherInterface d) {
        this(o, d, 4);
    }


    public NavigationBarOverlay(OsmInteractiveView o, DispatcherInterface d, int i) {
        super(o,new ControlBar(o.getContext(),
                getOrientation(BOTTOM), i), BOTTOM);

        buttonPlus = getBar().addImageButton(R.drawable.zoom_in);
        buttonMinus = getBar().addImageButton(R.drawable.zoom_out);
        View lock = getBar().addSolidIndexButton(
                new SolidPositionLock(o.getContext(),o.getSolidKey()));
        buttonFrame = getBar().addImageButton(R.drawable.zoom_fit_best);

        ToolTip.set(buttonPlus, R.string.tt_map_zoomin);
        ToolTip.set(buttonMinus,R.string.tt_map_zoomout);
        ToolTip.set(buttonFrame,  R.string.tt_map_frame);
        ToolTip.set(lock, R.string.tt_map_home);

        d.addTarget(this, InfoID.ALL);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);

        MapView map = getMapView();
        AbsOsmView osm = getOsmView();

        if (v==buttonPlus) {
            map.getController().zoomIn();
        } else if (v==buttonMinus) {
            map.getController().zoomOut();
        } else if (v==buttonFrame && infoCache.size()>0) {
            
            if (nextInBoundingCycle()) {
                osm.frameBoundingBox(infoCache.valueAt(boundingCycle).getBoundingBox());
                AppLog.i(getContext(), infoCache.valueAt(boundingCycle).getName());
            }
        }
    }

    
    private boolean nextInBoundingCycle() {
         int c = infoCache.size();
         
         while (c > 0) {
             c--;
             boundingCycle++;
             
             if (boundingCycle >= infoCache.size()) 
                 boundingCycle=0;
             
             if (       infoCache.valueAt(boundingCycle).getBoundingBox().hasBounding() 
                     || infoCache.valueAt(boundingCycle).getGpxList().getPointList().size()>0) 
                 return true;
         }
         return false;
    }
    
    

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        if (info.isLoaded()) {

            //AppLog.d(this, "add " + iid);
            infoCache.put(iid, info);

        } else {
            //AppLog.d(this, "remove " + iid);
            infoCache.remove(iid);
        }
    }
    
}
