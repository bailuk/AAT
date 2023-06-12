package ch.bailu.aat.menus;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuItem;

import org.mapsforge.core.model.LatLong;

import ch.bailu.aat.R;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.preferences.location.SolidGoToLocation;
import ch.bailu.aat.util.Clipboard;
import ch.bailu.aat_lib.coordinates.OlcCoordinates;
import ch.bailu.aat_lib.coordinates.WGS84Coordinates;
import ch.bailu.aat_lib.map.MapViewInterface;
import ch.bailu.aat_lib.preferences.map.SolidMapGrid;

public final class LocationMenu extends AbsMenu{

    private final MapViewInterface map;
    private final Context context;
    private final Clipboard clipboard;

    private MenuItem paste;


    public LocationMenu(Context c, MapViewInterface m) {
        map = m;
        context = c;
        clipboard = new Clipboard(context);
    }


    @Override
    public void inflate(Menu menu) {
        add(menu,R.string.location_send, this::send);
        add(menu,R.string.location_view, this::view);
        add(menu, R.string.clipboard_copy, this::copy);
        paste = add(menu, R.string.clipboard_paste, this::paste);
        add(menu, new SolidGoToLocation(context).getLabel(),
                ()->new SolidGoToLocation(context).goToLocationFromUser(map));
    }

    @Override
    public String getTitle() {
        return context.getString(R.string.location_title);
    }


    @Override
    public Drawable getIcon() {
        return null;
    }

     @Override
    public void prepare(Menu menu) {
        paste.setEnabled(clipboard.getText() != null);
    }


    private void paste() {
        final String s = clipboard.getText().toString();

        new SolidGoToLocation(context).goToLocation(map, s);
    }



    private void copy() {
        SolidMapGrid sgrid = new SolidMapGrid(new Storage(context),
                map.getMContext().getSolidKey());

        clipboard.setText(sgrid.getClipboardLabel(), sgrid.getCode(getCenter()));
    }


    private void view() {
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        final LatLong center = getCenter();
        final Uri uri = Uri.parse(WGS84Coordinates.getGeoUri(center));

        intent.setData(uri);
        context.startActivity(Intent.createChooser(intent, uri.toString()));
    }


    private void send() {
        final Intent intent = new Intent(Intent.ACTION_SEND);
        final LatLong center = getCenter();


        final String url = WGS84Coordinates.getGeoUri(center);
        final String desc = WGS84Coordinates.getGeoPointDescription(center);
        final String body = desc+ "\n\n" + url + "\n\n" + new OlcCoordinates(center).toString();


        intent.setType("label/plain");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, url);
        intent.putExtra(android.content.Intent.EXTRA_TEXT, body);
        context.startActivity(Intent.createChooser(intent, url));
    }

    private LatLong getCenter() {
        return map.getMapViewPosition().getCenter();
    }

}
