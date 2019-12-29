package ch.bailu.aat.menus;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;

import ch.bailu.aat.R;
import ch.bailu.aat.activities.ActivitySwitcher;
import ch.bailu.aat.activities.NominatimActivity;
import ch.bailu.aat.activities.OverpassActivity;
import ch.bailu.aat.activities.PoiActivity;
import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.util.ToDo;

public class MapSearchMenu extends AbsMenu {

    private final MapContext mcontext;
    private final Context context;


    private MenuItem nominatim, overpass, poi;


    public MapSearchMenu(MapContext m) {
        mcontext = m;
        context = m.getContext();
    }

    @Override
    public void prepare(Menu menu){}

    @Override
    public void inflate(Menu menu) {

        nominatim = menu.add(R.string.intro_nominatim);
        overpass = menu.add(R.string.query_overpass);
        poi = menu.add(ToDo.translate("Poi (offline)"));
    }

    @Override
    public String getTitle() {
        return ToDo.translate("Map search");
    }


    @Override
    public Drawable getIcon() {
        return null;
    }



    @Override
    public boolean onItemClick(MenuItem item) {
        if (item == overpass) {
            ActivitySwitcher.start(context,
                    OverpassActivity.class,
                    mcontext.getMetrics().getBoundingBox());

        } else if (item == nominatim) {
            ActivitySwitcher.start(context,
                    NominatimActivity.class,
                    mcontext.getMetrics().getBoundingBox());

        } else if (item == poi) {
            ActivitySwitcher.start(context,
                    PoiActivity.class,
                    mcontext.getMetrics().getBoundingBox());
        }
        return false;
    }

}
