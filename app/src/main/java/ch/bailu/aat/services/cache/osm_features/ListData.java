package ch.bailu.aat.services.cache.osm_features;

import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.icons.IconMapService;
import ch.bailu.aat.util.AppHtml;
import ch.bailu.aat.util.filter_list.KeyList;
import ch.bailu.aat.util.ui.AppLog;


public class ListData {
    public final String name, key, value;
    public final Spanned paragraph;

    public final KeyList keys;

    public ListData(final ServiceContext scontext, MapFeaturesParser parser, final Icon icons) {
        IconMapService map = scontext.getIconMapService();

        name = parser.getName();
        key = parser.getKey();
        value = parser.getValue();

        StringBuilder html = new StringBuilder();
        map.iconify(html, parser.getKey(), parser.getValue());
        parser.toHtml(html);

        paragraph = AppHtml.fromHtml(html.toString(), new Html.ImageGetter() {

            @Override
            public Drawable getDrawable(String source) {

                if (source.endsWith(".svg")) {
                    return icons.toDrawable(scontext.getContext(), source);

                } else if (source.contains("way")) {
                    return icons.way;

                } else if (source.contains("node")) {
                    return icons.node;

                } else if (source.contains("area")) {
                    return icons.area;

                } else if (source.contains("relation")) {
                    return icons.relation;

                } else {
                    AppLog.d(this, source);
                }


                return null;
            }
        });

        keys = new KeyList();
        keys.addKeys(name);
        keys.addKeys(key);
        keys.addKeys(value);
        keys.addKeys(paragraph.toString());

    }
}
