package ch.bailu.aat.menus;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Menu;

import ch.bailu.aat.R;
import ch.bailu.aat.app.ActivitySwitcher;
import ch.bailu.aat.activities.NominatimActivity;
import ch.bailu.aat.activities.OverpassActivity;
import ch.bailu.aat.activities.PoiActivity;
import ch.bailu.aat.util.NominatimApi;
import ch.bailu.aat.util.OverpassApi;
import ch.bailu.aat_lib.map.MapContext;

public class MapQueryMenu extends AbsMenu {

    private final MapContext mcontext;
    private final Context context;

    public MapQueryMenu(Context c, MapContext mc) {
        mcontext = mc;
        context = c;
    }

    @Override
    public void prepare(Menu menu) {
    }

    @Override
    public void inflate(Menu menu) {
        add(menu, NominatimApi.API_NAME, ()->
                ActivitySwitcher.start(context,
                        NominatimActivity.class,
                        mcontext.getMetrics().getBoundingBox()));

        add(menu, OverpassApi.Companion.getName(context), ()->
                ActivitySwitcher.start(context,
                        OverpassActivity.class,
                        mcontext.getMetrics().getBoundingBox()));


        add(menu, R.string.p_mapsforge_poi, ()->
                ActivitySwitcher.start(context,
                        PoiActivity.class,
                        mcontext.getMetrics().getBoundingBox()));
    }

    @Override
    public String getTitle() {
        return context.getString(R.string.intro_nominatim);
    }


    @Override
    public Drawable getIcon() {
        return null;
    }
}
