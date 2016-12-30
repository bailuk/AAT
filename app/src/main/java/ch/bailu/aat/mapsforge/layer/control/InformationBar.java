package ch.bailu.aat.mapsforge.layer.control;

import android.view.View;
import android.widget.ImageButton;

import ch.bailu.aat.R;
import ch.bailu.aat.activities.ActivitySwitcher;
import ch.bailu.aat.activities.NominatimActivity;
import ch.bailu.aat.activities.OverpassActivity;
import ch.bailu.aat.dispatcher.DispatcherInterface;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.helpers.ToolTip;
import ch.bailu.aat.mapsforge.layer.ContextLayer;
import ch.bailu.aat.preferences.SolidIndexList;
import ch.bailu.aat.preferences.SolidLegend;
import ch.bailu.aat.preferences.SolidMapGrid;

public class InformationBar extends ControlBar {
    private final View reload;

    private final ImageButton overpass, nominatim, location;

    private final NodeViewLayer selector;

    private final ContextLayer clayer;



    public InformationBar(ContextLayer cl, DispatcherInterface d) {
        super(cl.getMapView(),new ch.bailu.aat.views.ControlBar(cl.getContext(), getOrientation(RIGHT)), RIGHT);

        clayer = cl;
        final SolidIndexList sgrid, slegend;

        sgrid = new SolidMapGrid(cl.getContext(), cl.getMapView().getSolidKey());
        slegend = new SolidLegend(cl.getContext(), cl.getMapView().getSolidKey());

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
            ActivitySwitcher.start(clayer.getContext(), OverpassActivity.class, clayer.getMapView().getBoundingBox());
        } else if (v==nominatim) {
            ActivitySwitcher.start(clayer.getContext(), NominatimActivity.class, clayer.getMapView().getBoundingBox());
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

}
