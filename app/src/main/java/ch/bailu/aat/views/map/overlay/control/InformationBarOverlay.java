package ch.bailu.aat.views.map.overlay.control;

import android.view.View;
import android.widget.ImageButton;

import ch.bailu.aat.R;
import ch.bailu.aat.activities.ActivitySwitcher;
import ch.bailu.aat.activities.NominatimActivity;
import ch.bailu.aat.activities.OverpassActivity;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.helpers.ToolTip;
import ch.bailu.aat.menus.LocationMenu;
import ch.bailu.aat.preferences.SolidIndexList;
import ch.bailu.aat.preferences.SolidLegend;
import ch.bailu.aat.preferences.SolidMapGrid;
import ch.bailu.aat.views.ControlBar;
import ch.bailu.aat.views.map.OsmInteractiveView;
import ch.bailu.aat.views.map.overlay.MapPainter;
import ch.bailu.aat.views.map.overlay.gpx.InfoViewNodeSelectorOverlay;

public class InformationBarOverlay extends ControlBarOverlay {
    private final View reload;

    private final ImageButton overpass, nominatim, location;

    private final InfoViewNodeSelectorOverlay selector;



    public InformationBarOverlay(OsmInteractiveView o) {
        super(o,new ControlBar(o.getContext(), ControlBar.VERTICAL));

        final SolidIndexList sgrid, slegend;

        sgrid = new SolidMapGrid(o.getContext(), o.solidKey);
        slegend = new SolidLegend(o.getContext(), o.solidKey);

        ControlBar bar = getBar();
        View grid=bar.addSolidIndexButton(sgrid);
        View legend=bar.addSolidIndexButton(slegend);

        overpass = bar.addImageButton(R.drawable.go_bottom);
        nominatim = bar.addImageButton(R.drawable.edit_find);
        reload = bar.addImageButton(R.drawable.view_refresh);
        location = bar.addImageButton(R.drawable.find_location);

        selector = new InfoViewNodeSelectorOverlay(o, GpxInformation.ID.INFO_ID_ALL);


        ToolTip.set(grid,R.string.tt_info_grid);
        ToolTip.set(legend,R.string.tt_info_legend);
        ToolTip.set(nominatim,R.string.tt_info_nominatim);
        ToolTip.set(overpass,R.string.tt_info_overpass);
        ToolTip.set(reload,R.string.tt_info_reload);
        ToolTip.set(location, R.string.tt_info_location);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);

        if (v==reload) {
            getMapView().getTileProvider().deleteVisibleTilesFromDisk();


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
    public void rightTab() {
        showBar();
    }

    @Override
    public void showBar() {
        showBarAtRight();
        selector.showAtLeft();
    }

    @Override
    public void run() {}

    @Override
    public void hideBar() {
        super.hideBar();
        selector.hide();
    }

    @Override
    public void onContentUpdated(GpxInformation info) {
        selector.onContentUpdated(info);

    }


}