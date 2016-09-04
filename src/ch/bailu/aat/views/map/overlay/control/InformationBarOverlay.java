package ch.bailu.aat.views.map.overlay.control;

import org.osmdroid.util.GeoPoint;

import android.content.Intent;
import android.net.Uri;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import ch.bailu.aat.R;
import ch.bailu.aat.activities.ActivitySwitcher;
import ch.bailu.aat.activities.NominatimActivity;
import ch.bailu.aat.activities.OverpassActivity;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.helpers.ToolTip;
import ch.bailu.aat.preferences.SolidIndexList;
import ch.bailu.aat.preferences.SolidLegend;
import ch.bailu.aat.preferences.SolidMapGrid;
import ch.bailu.aat.views.ControlBar;
import ch.bailu.aat.views.map.OsmInteractiveView;
import ch.bailu.aat.views.map.overlay.MapPainter;
import ch.bailu.aat.views.map.overlay.gpx.InfoViewNodeSelectorOverlay;

public class InformationBarOverlay extends ControlBarOverlay {
    private final View reload;

    private final ImageButton overpass, nominatim, sendto;
    private final SolidIndexList sgrid, slegend;

    private final InfoViewNodeSelectorOverlay selector;



    public InformationBarOverlay(OsmInteractiveView o) {
        super(o,new ControlBar(o.getContext(), ControlBar.VERTICAL));

        sgrid = new SolidMapGrid(o.getContext(), o.solidKey);
        slegend = new SolidLegend(o.getContext(), o.solidKey);

        ControlBar bar = getBar();
        View grid=bar.addSolidIndexButton(sgrid);
        View legend=bar.addSolidIndexButton(slegend);

        overpass = bar.addImageButton(R.drawable.go_bottom);
        nominatim = bar.addImageButton(R.drawable.edit_find);
        reload = bar.addImageButton(R.drawable.view_refresh);

        sendto = bar.addImageButton(R.drawable.send_to);

        selector = new InfoViewNodeSelectorOverlay(o, GpxInformation.ID.INFO_ID_ALL);


        ToolTip.set(grid,R.string.tt_info_grid);
        ToolTip.set(legend,R.string.tt_info_legend);
        ToolTip.set(nominatim,R.string.tt_info_nominatim);
        ToolTip.set(overpass,R.string.tt_info_overpass);
        ToolTip.set(reload,R.string.tt_info_reload);

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
        } else if (v==sendto) {
            new LocationAction().showPopupMenu(v);
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
    public void updateGpxContent(GpxInformation info) {
        selector.updateGpxContent(info);

    }

    private class LocationAction implements OnMenuItemClickListener {


        public void showPopupMenu(View v) {
            final PopupMenu popup = new PopupMenu(getContext(), v);
            final MenuInflater inflater = popup.getMenuInflater();

            inflater.inflate(R.menu.sendlocation, popup.getMenu());
            popup.setOnMenuItemClickListener(this);
            popup.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (item.getItemId()== R.id.m_location_send) {
                send();

            } else if (item.getItemId() == R.id.m_location_view) {
                view();

            }
            return false;
        }

        private void view() {
            final Intent intent = new Intent(Intent.ACTION_VIEW);


            final GeoPoint center = getMapView().getBoundingBox().getCenter();
            final double la = center.getLatitudeE6() / 1e6d;
            final double lo = center.getLongitudeE6() / 1e6d;

            final Uri uri = Uri.parse("geo:" + la + "," + lo);

            AppLog.d(this, uri.toString());

            intent.setData(uri);
            getContext().startActivity(Intent.createChooser(intent, uri.toString()));
        }


        private void send() {
            final Intent intent = new Intent(Intent.ACTION_SEND);
            final GeoPoint center = getMapView().getBoundingBox().getCenter();
            final double la = center.getLatitudeE6() / 1e6d;
            final double lo = center.getLongitudeE6() / 1e6d;

            final String url = "geo:" + la + "," + lo;
            final String body = "latitude: "+ la + " longitude: " + lo + "\n\n" + url;   


            intent.setType("text/plain");
            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, url);
            intent.putExtra(android.content.Intent.EXTRA_TEXT, body);
            getContext().startActivity(Intent.createChooser(intent, url));
        }

    }


}