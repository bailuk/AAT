package ch.bailu.aat.views.map.overlay;

import org.osmdroid.views.MapView;

import android.view.MotionEvent;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.views.map.AbsOsmView;

public class OverlayList extends OsmOverlay {
    private OsmOverlay[] overlayList=new OsmOverlay[]{};


    public OverlayList(AbsOsmView absOsmView, OsmOverlay[] list) {
        super(absOsmView);
        overlayList = list;
    }


    @Override
    public boolean onSingleTapConfirmed(MotionEvent e, MapView map) {
        for (OsmOverlay overlay: overlayList) 
            overlay.onSingleTapConfirmed(e, map);

        return false;
    }


    @Override
    public void draw(MapPainter painter) {
        for (OsmOverlay overlay: overlayList) 
            overlay.draw(painter);
    }


    @Override
    public void updateGpxContent(GpxInformation info) {
        for (OsmOverlay overlay: overlayList) 
            overlay.updateGpxContent(info);
    }



    @Override
    public void onSharedPreferenceChanged(String key) {
        for (OsmOverlay overlay: overlayList) 
            overlay.onSharedPreferenceChanged(key);
    }

}
