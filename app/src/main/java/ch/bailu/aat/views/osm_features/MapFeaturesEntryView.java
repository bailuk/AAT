package ch.bailu.aat.views.osm_features;


import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import ch.bailu.aat.R;
import ch.bailu.aat.gpx.attributes.Keys;
import ch.bailu.aat.menus.MapFeaturesMenu;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.osm_features.MapFeaturesListEntry;
import ch.bailu.aat.services.icons.IconMapService;
import ch.bailu.aat.util.AppHtml;
import ch.bailu.aat.util.ui.AppDensity;
import ch.bailu.aat.views.SVGAssetView;

public class MapFeaturesEntryView extends LinearLayout implements View.OnClickListener {
    private static int icon_view_size = 0;

    private final TextView text;
    private final SVGAssetView icon;

    private final MapFeaturesListView.OnSelected onSelected;

    private MapFeaturesListEntry entry;


    public MapFeaturesEntryView(final ServiceContext scontext, MapFeaturesListView.OnSelected s) {
        super(scontext.getContext());


        if (icon_view_size == 0)
            icon_view_size = new AppDensity(getContext())
                    .toPixel_i(IconMapService.BIG_ICON_SIZE+10);


        onSelected = s;
        text = new TextView(getContext());
        icon = new SVGAssetView(scontext, R.drawable.open_menu_light);
        icon.setOnClickListener(this);

        //AppTheme.themify(icon);


        addView(icon, icon_view_size, icon_view_size);
        addView(text);


    }



    public void set(final MapFeaturesListEntry e) {
        entry = e;
        icon.setImageObject(Keys.toIndex(entry.key), entry.value);
        text.setText(AppHtml.fromHtml(entry.html));
    }


    @Override
    public void onClick(View view) {
        if (view == icon)
            new MapFeaturesMenu(entry, onSelected).showAsPopup(getContext(), view);
    }
}
