package ch.bailu.aat.views.map.overlay;

import android.view.MotionEvent;

import org.osmdroid.views.MapView;

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
    public void onContentUpdated(GpxInformation info) {
        for (OsmOverlay overlay: overlayList) 
            overlay.onContentUpdated(info);
    }



    @Override
    public void onSharedPreferenceChanged(String key) {
        for (OsmOverlay overlay: overlayList) 
            overlay.onSharedPreferenceChanged(key);
    }

    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        for (OsmOverlay overlay: overlayList)
            overlay.onLayout(changed, l, t, r, b);
    }
}
