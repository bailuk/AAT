package ch.bailu.aat.mapsforge.layer.control;

import android.util.SparseArray;
import android.view.View;

import org.mapsforge.core.model.LatLong;

import ch.bailu.aat.R;
import ch.bailu.aat.dispatcher.DispatcherInterface;
import ch.bailu.aat.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.helpers.ToolTip;
import ch.bailu.aat.mapsforge.layer.context.MapContext;
import ch.bailu.aat.preferences.SolidPositionLock;

public class NavigationBar extends ControlBar implements OnContentUpdatedInterface {

    private final MapContext mcontext;
    private final View buttonPlus;
    private final View buttonMinus;
    private final View buttonFrame;

    private final SparseArray<GpxInformation> infoCache = new SparseArray<>(10);

    private int boundingCycle=0;


    public NavigationBar(MapContext mc, DispatcherInterface d) {
        this(mc, d, 4);
    }


    public NavigationBar(MapContext mc, DispatcherInterface d, int i) {
        super(mc.mapView,new ch.bailu.aat.views.ControlBar(mc.context,
                getOrientation(BOTTOM), i), BOTTOM);

        mcontext = mc;

        buttonPlus = getBar().addImageButton(R.drawable.zoom_in);
        buttonMinus = getBar().addImageButton(R.drawable.zoom_out);
        View lock = getBar().addSolidIndexButton(
                new SolidPositionLock(mc.context, mc.skey));
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


        if (v==buttonPlus) {
           mcontext.mapView.getModel().mapViewPosition.zoomIn();

        } else if (v==buttonMinus) {
            mcontext.mapView.getModel().mapViewPosition.zoomOut();

        } else if (v==buttonFrame && infoCache.size()>0) {

            if (nextInBoundingCycle()) {


                LatLong c =
                        infoCache.valueAt(boundingCycle).getBoundingBox().toBoundingBox()
                                .getCenterPoint();

                mcontext.mapView.setCenter(c);
                AppLog.i(mcontext.context, infoCache.valueAt(boundingCycle).getName());

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

            infoCache.put(iid, info);

        } else {
            infoCache.remove(iid);
        }
    }

}
