package ch.bailu.aat.views.osm_features;


import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import java.io.IOException;

import ch.bailu.aat.R;
import ch.bailu.aat.menus.MapFeaturesMenu;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.osm_features.MapFeaturesListEntry;
import ch.bailu.aat.services.icons.IconMapService;
import ch.bailu.aat.util.AppHtml;
import ch.bailu.aat.util.graphic.SyncTileBitmap;
import ch.bailu.aat.util.ui.AppDensity;
import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.views.SVGAssetView;

public class MapFeaturesEntryView extends LinearLayout implements View.OnClickListener {
    private static Config config = null;

    private final TextView text;
    private final SVGAssetView icon;

    private final MapFeaturesListView.OnSelected onSelected;

    private MapFeaturesListEntry entry;



    private static class Config {
        private final int icon_view_size;

        public final Drawable way, node, area, relation;

        public Config(Context c) {
            int sicon_size = new AppDensity(c).toDPi(IconMapService.SMALL_ICON_SIZE);
            icon_view_size = new AppDensity(c).toDPi(IconMapService.BIG_ICON_SIZE+10);


            way = toDrawable(c,"way.svg",sicon_size, Color.WHITE);
            node = toDrawable(c,"symbols/node.svg",sicon_size,Color.WHITE);
            area = toDrawable(c,"symbols/area.svg",sicon_size,Color.WHITE);
            relation = toDrawable(c,"symbols/relation.svg",sicon_size,Color.WHITE);

        }

        public static Drawable toDrawable(Context context, String asset, int size, int color) {
            AssetManager assets = context.getAssets();
            Resources res = context.getResources();

            try {
                SVG svg = SVG.getFromAsset(assets, asset);
                return SyncTileBitmap.toDrawable(svg, size, res, color);

            } catch (SVGParseException | IOException e) {
                AppLog.d(asset,asset);
            }
            return null;
        }
    }



    public MapFeaturesEntryView(final ServiceContext scontext, MapFeaturesListView.OnSelected s) {
        super(scontext.getContext());


        if (config == null)
            config = new Config(getContext());


        onSelected = s;
        text = new TextView(getContext());
        icon = new SVGAssetView(scontext, R.drawable.open_menu_light);
        icon.setOnClickListener(this);
        AppTheme.themify(icon);


        addView(icon, config.icon_view_size, config.icon_view_size);
        addView(text);


    }



    public void set(final MapFeaturesListEntry e) {
        entry = e;
        icon.setImageObject(entry.key, entry.value);

        Spanned paragraph = AppHtml.fromHtml(entry.html, new Html.ImageGetter() {

            @Override
            public Drawable getDrawable(String source) {

                if (source.contains("way")) {
                    return config.way;

                } else if (source.contains("node")) {
                    return config.node;

                } else if (source.contains("area")) {
                    return config.area;

                } else if (source.contains("relation")) {
                    return config.relation;

                } else {
                    AppLog.d(this, source);
                }


                return null;
            }
        });
        text.setText(paragraph);
    }


    @Override
    public void onClick(View view) {
        if (view == icon)
            new MapFeaturesMenu(entry, onSelected).showAsPopup(getContext(), view);

    }
}
