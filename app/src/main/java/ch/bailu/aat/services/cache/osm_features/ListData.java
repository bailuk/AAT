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
    public final String name, key, value, summaryKey;
    public final Spanned paragraph;

    public final KeyList keys;

    public ListData(final ServiceContext scontext, MapFeaturesParser parser, final Icon icons) {
        IconMapService map = scontext.getIconMapService();

        name = parser.getName();
        key = parser.getKey();
        value = parser.getValue();

        StringBuilder builder = new StringBuilder();
        map.iconify(builder, parser.getKey(), parser.getValue());

        String html = parser.addHtml(builder).toString();

        paragraph = AppHtml.fromHtml(html, new Html.ImageGetter() {

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

        summaryKey = parser.getSummaryKey();

        keys = new KeyList(summaryKey);

        if (isSummary() == false)
            keys.addKeys(html);

    }

    public String getQueryString() {
        return "["+key+"="+value+"]";
    }

    public boolean isSummary() {
        return key.isEmpty() && value.isEmpty();
    }

    public int length() {
        int l = name.length() +
                key.length() +
                summaryKey.length() +
                paragraph.length() +
                keys.length();

        return l;
    }
}
