package ch.bailu.aat.views.map.overlay.control;

import org.osmdroid.views.MapView;

import android.util.SparseArray;
import android.view.View;
import android.widget.LinearLayout;

import ch.bailu.aat.R;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.helpers.ToolTip;
import ch.bailu.aat.preferences.SolidPositionLock;
import ch.bailu.aat.views.ControlBar;
import ch.bailu.aat.views.map.AbsOsmView;
import ch.bailu.aat.views.map.OsmInteractiveView;


public class NavigationBarOverlay extends ControlBarOverlay {
    private final View buttonPlus;
    private final View buttonMinus;
    private final View buttonFrame;

    private final SparseArray<GpxInformation> infoCache = new SparseArray<>(10);

    private int boundingCycle=0;


    public NavigationBarOverlay(OsmInteractiveView o) {
        this(o, 4);
    }


    public NavigationBarOverlay(OsmInteractiveView o, int i) {
        super(o,new ControlBar(o.getContext(),
                LinearLayout.HORIZONTAL, i));

        buttonPlus = getBar().addImageButton(R.drawable.zoom_in);
        buttonMinus = getBar().addImageButton(R.drawable.zoom_out);
        View lock = getBar().addSolidIndexButton(new SolidPositionLock(getMapView().getContext(),o.solidKey));
        buttonFrame = getBar().addImageButton(R.drawable.zoom_fit_best);

        ToolTip.set(buttonPlus, R.string.tt_map_zoomin);
        ToolTip.set(buttonMinus,R.string.tt_map_zoomout);
        ToolTip.set(buttonFrame,  R.string.tt_map_frame);
        ToolTip.set(lock, R.string.tt_map_home);
        
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
    public void bottomTap() {
        showBar();
    }


    @Override
    public void showBar() {
        showBarAtBottom();
    }


    @Override
    public void updateGpxContent(GpxInformation info) {
        if (info.isLoaded()) {
            infoCache.put(info.getID(), info);

        } else {
            infoCache.remove(info.getID());
        }
    }
    
}
