package ch.bailu.aat.map.osm.overlay.control;

import android.view.View;
import android.widget.ImageButton;

import ch.bailu.aat.R;
import ch.bailu.aat.activities.ActivitySwitcher;
import ch.bailu.aat.activities.NominatimActivity;
import ch.bailu.aat.activities.OverpassActivity;
import ch.bailu.aat.dispatcher.DispatcherInterface;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.util.ui.ToolTip;
import ch.bailu.aat.menus.LocationMenu;
import ch.bailu.aat.preferences.SolidIndexList;
import ch.bailu.aat.preferences.SolidLegend;
import ch.bailu.aat.preferences.SolidMapGrid;
import ch.bailu.aat.views.ControlBar;
import ch.bailu.aat.map.osm.OsmInteractiveView;
import ch.bailu.aat.map.osm.overlay.MapPainter;
import ch.bailu.aat.map.osm.overlay.gpx.AutoNodeViewOverlay;
import ch.bailu.aat.map.osm.overlay.gpx.NodeViewOverlay;

public class InformationBarOverlay extends ControlBarOverlay {
    private final View reload;

    private final ImageButton overpass, nominatim, location;

    private final NodeViewOverlay selector;



    public InformationBarOverlay(OsmInteractiveView o, DispatcherInterface d) {
        super(o,new ControlBar(o.getContext(), getOrientation(RIGHT)), RIGHT);

        final SolidIndexList sgrid, slegend;

        sgrid = new SolidMapGrid(o.getContext(), o.getSolidKey());
        slegend = new SolidLegend(o.getContext(), o.getSolidKey());

        ControlBar bar = getBar();
        View grid=bar.addSolidIndexButton(sgrid);
        View legend=bar.addSolidIndexButton(slegend);

        overpass = bar.addImageButton(R.drawable.go_bottom);
        nominatim = bar.addImageButton(R.drawable.edit_find);
        reload = bar.addImageButton(R.drawable.view_refresh);
        location = bar.addImageButton(R.drawable.find_location);

        selector = new AutoNodeViewOverlay(o);


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
            getMapView().getTileProvider().reDownloadTiles();


        } else if (v==overpass) {
            ActivitySwitcher.start(getContext(), OverpassActivity.class, getMapView().getBoundingBox());
        } else if (v==nominatim) {
            ActivitySwitcher.start(getContext(), NominatimActivity.class, getMapView().getBoundingBox());
        } else if (v==location) {
            new LocationMenu(getMapView()).showAsPopup(getContext(), location);
        }



    }


    @Override
    public void draw(MapPainter p) {
        if (isVisible()) selector.draw(p);
    }


    @Override
    public void onShowBar() {
        selector.showAtLeft();
    }

    @Override
    public void run() {}

    @Override
    public void onHideBar() {
        selector.hide();
    }



}