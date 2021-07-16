package ch.bailu.aat.menus;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Menu;

import ch.bailu.aat.R;
import ch.bailu.aat.activities.ActivitySwitcher;
import ch.bailu.aat.activities.NominatimActivity;
import ch.bailu.aat.activities.OverpassActivity;
import ch.bailu.aat.activities.PoiActivity;
import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.util.NominatimApi;
import ch.bailu.aat.util.OverpassApi;

public class MapQueryMenu extends AbsMenu {

    private final MapContext mcontext;
    private final Context context;

    public MapQueryMenu(MapContext m) {
        mcontext = m;
        context = m.getContext();
    }

    @Override
    public void prepare(Menu menu) {
    }

    @Override
    public void inflate(Menu menu) {
        add(menu, NominatimApi.NAME, ()->
                ActivitySwitcher.start(context,
                        NominatimActivity.class,
                        mcontext.getMetrics().getBoundingBox()));

        add(menu, OverpassApi.getName(context), ()->
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

