package ch.bailu.aat.menus;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import ch.bailu.aat.R;
import ch.bailu.aat.coordinates.Coordinates;
import ch.bailu.aat.helpers.AppLog;

public class LocationMenu extends AbsMenu{

    private final MapView map;
    private final Context context;

    private MenuItem send, view, copy, paste;
    
    
    public LocationMenu(MapView m) {
        map = m;
        context = m.getContext();
    }
    
    @Override
    public void inflate(Menu menu) {
        send = menu.add(R.string.location_send);
        view = menu.add(R.string.location_view);
        copy = menu.add(R.string.location_copy);
        paste = menu.add(R.string.location_paste);
        
    }

    @Override
    public void inflateWithHeader(ContextMenu menu) {
        menu.setHeaderTitle("Center Location*");
        
    }

    @Override
    public void prepare(Menu menu) {
        
        paste.setEnabled(getStringFromClipboard() != null);
    }

    
    private String getStringFromClipboard() {
        ClipboardManager clipboard = (ClipboardManager)
                context.getSystemService(Context.CLIPBOARD_SERVICE);

        ClipData clip = clipboard.getPrimaryClip();
        if (clip != null) {
            ClipData.Item i = clipboard.getPrimaryClip().getItemAt(0);
            if (i!= null) {
                CharSequence t = i.getText();
                if (t != null) return t.toString();
            }
        }
        return null; 
    }
        
    
    
    @Override
    public boolean onItemClick(MenuItem item) {
        if (item == send) {
            send();

        } else if (item == view) {
            view();

        } else if (item == copy) {
            copy();

        } else if (item == paste) {
            paste();
        }
        return false;
    }

    

    private void paste() {
        GeoPoint geo = new GeoPoint(0,0);
        
        String s = getStringFromClipboard();
        if (s!=null  && Coordinates.stringToGeoPoint(s, geo)) {
            map.getController().setCenter(geo);
        }
        
    }

    private void copy() {
        ClipboardManager clipboard = (ClipboardManager)
                context.getSystemService(Context.CLIPBOARD_SERVICE);

        ClipData clip = ClipData.newPlainText("GEO location", Coordinates.geoPointToGeoUri(getCenter()));
        clipboard.setPrimaryClip(clip);
        
    }


    private void view() {
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        final GeoPoint center = getCenter();
        final Uri uri = Uri.parse(Coordinates.geoPointToGeoUri(center));
        AppLog.d(this, uri.toString());

        intent.setData(uri);
        context.startActivity(Intent.createChooser(intent, uri.toString()));
    }


    private void send() {
        final Intent intent = new Intent(Intent.ACTION_SEND);
        final GeoPoint center = getCenter();

        final String url = Coordinates.geoPointToGeoUri(center);
        final String desc = Coordinates.geoPointToDescription(center);
        final String body = desc+ "\n\n" + url;   


        intent.setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, url);
        intent.putExtra(android.content.Intent.EXTRA_TEXT, body);
        context.startActivity(Intent.createChooser(intent, url));
    }
    
    private GeoPoint getCenter() {
        return map.getBoundingBox().getCenter();
    }
    
}
