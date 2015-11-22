package ch.bailu.aat.views.map.overlay.control;

import org.osmdroid.views.MapView;

import android.util.SparseArray;
import android.view.View;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.helpers.AppLayout;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.preferences.SolidPositionLock;
import ch.bailu.aat.views.ControlBar;
import ch.bailu.aat.views.map.AbsOsmView;
import ch.bailu.aat.views.map.OsmInteractiveView;
import ch.bailu.aat.R;


public class NavigationBarOverlay extends ControlBarOverlay implements GpxInformation.ID {
    private View buttonPlus, buttonMinus, buttonFrame;

    private final SparseArray<GpxInformation> infoCache = new SparseArray<GpxInformation>(10);

    private int boundingCycle=0;


    public NavigationBarOverlay(OsmInteractiveView o) {
        this(o, 4);
    }


    public NavigationBarOverlay(OsmInteractiveView o, int i) {
        super(o,new ControlBar(o.getContext(), 
                AppLayout.getOrientationAlongSmallSide(o.getContext()), i));

        buttonPlus = getBar().addImageButton(R.drawable.zoom_in);
        buttonMinus = getBar().addImageButton(R.drawable.zoom_out);
        getBar().addSolidIndexButton(new SolidPositionLock(getMapView().getContext(),o.solidKey));
        buttonFrame = getBar().addImageButton(R.drawable.zoom_fit_best);

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
            
            boundingCycle++;
            if (boundingCycle >= infoCache.size()) 
                boundingCycle=0;
            
            
            osm.frameBoundingBox(infoCache.valueAt(boundingCycle).getBoundingBox());
            AppLog.i(getContext(), infoCache.valueAt(boundingCycle).getName());
        }
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
