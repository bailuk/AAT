package ch.bailu.aat.services.cache.osm_features;

import java.util.ArrayList;

import ch.bailu.aat.util.filter_list.KeyList;


public class MapFeaturesListEntry {
    private final boolean isSummary;
    public final String name, key, value, summarySearchKey, summaryKey,  html;
    public final KeyList keys;
    public final int id;

    public MapFeaturesListEntry(MapFeaturesParser parser) {

        String k = parser.getKey();
        isSummary = k.isEmpty();
        name = parser.getName();

        if (isSummary) {
            key = parser.getSumaryKey();
        } else {
            key = k;
        }

        value = parser.getValue();
        id = parser.getId();
        summaryKey = parser.getSumaryKey();
        html = parser.addHtml(new StringBuilder()).toString();

        summarySearchKey = parser.getSummarySearchKey();
        keys = new KeyList(summarySearchKey);

        if (isSummary() == false)
            keys.addKeys(html);

    }

    public boolean isSummary() {
        return isSummary;
    }

    public int length() {
        int l = name.length() +
                key.length() +
                html.length() +
                keys.length();

        return l;
    }

    public String getDefaultQuery() {
        if (!key.isEmpty()) {
            if (!value.isEmpty()) {
                return "["+ key +"=\""+value+"\"];";
            }
            return "["+key+"];";
        }
        return "";
    }

    public ArrayList<String> getVariants() {
        ArrayList<String> list = new ArrayList<>(10);

        list.add(getDefaultQuery());
        if (isSummary == false) {
            list.add("["+ key +"~\"" + value + "\",i];");
        }
        return list;
    }
}
