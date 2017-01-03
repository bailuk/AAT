package ch.bailu.aat.map.mapsforge.layer.control;

import android.view.View;
import android.widget.ImageButton;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Point;

import ch.bailu.aat.R;
import ch.bailu.aat.activities.ActivitySwitcher;
import ch.bailu.aat.activities.NominatimActivity;
import ch.bailu.aat.activities.OverpassActivity;
import ch.bailu.aat.dispatcher.DispatcherInterface;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.util.ui.ToolTip;
import ch.bailu.aat.map.mapsforge.layer.context.MapContext;
import ch.bailu.aat.preferences.SolidIndexList;
import ch.bailu.aat.preferences.SolidLegend;
import ch.bailu.aat.preferences.SolidMapGrid;

public class InformationBar extends ControlBarLayer {
    private final View reload;

    private final ImageButton overpass, nominatim, location;

    private final NodeViewLayer selector;

    private final MapContext mcontext;



    public InformationBar(MapContext cl, DispatcherInterface d) {
        super(cl.mapView,new ch.bailu.aat.views.ControlBar(cl.context, getOrientation(RIGHT)), RIGHT);

        mcontext = cl;
        final SolidIndexList sgrid, slegend;

        sgrid = new SolidMapGrid(cl.context, cl.skey);
        slegend = new SolidLegend(cl.context, cl.skey);

        ch.bailu.aat.views.ControlBar bar = getBar();
        View grid=bar.addSolidIndexButton(sgrid);
        View legend=bar.addSolidIndexButton(slegend);

        overpass = bar.addImageButton(R.drawable.go_bottom);
        nominatim = bar.addImageButton(R.drawable.edit_find);
        reload = bar.addImageButton(R.drawable.view_refresh);
        location = bar.addImageButton(R.drawable.find_location);

        selector = new AutoNodeViewLayer(cl);


        ToolTip.set(grid,R.string.tt_info_grid);
        ToolTip.set(legend,R.string.tt_info_legend);
        ToolTip.set(nominatim,R.string.tt_info_nominatim);
        ToolTip.set(overpass,R.string.tt_info_overpass);
        ToolTip.set(reload,R.string.tt_info_reload);
        ToolTip.set(location, R.string.tt_info_location);

        d.addTarget(selector, InfoID.ALL);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);

        if (v==reload) {
            //getMapView().getTileProvider().reDownloadTiles();


        } else if (v==overpass) {
            ActivitySwitcher.start(mcontext.context, OverpassActivity.class, mcontext.mapView.getBoundingBox());
        } else if (v==nominatim) {
            ActivitySwitcher.start(mcontext.context, NominatimActivity.class, mcontext.mapView.getBoundingBox());
        } else if (v==location) {
            //new LocationMenu(getMapView()).showAsPopup(getContext(), location);
        }



    }

    @Override
    public void onShowBar() {
        selector.showAtLeft();
    }


    @Override
    public void onHideBar() {
        selector.hide();
    }

    @Override
    public void draw(BoundingBox b, byte z, Canvas c, Point t) {
        if (isBarVisible()) selector.draw(b, z, c, t);
    }
}
