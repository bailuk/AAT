package ch.bailu.aat.services.cache.osm_features;

import java.util.ArrayList;

import ch.bailu.aat_lib.lib.filter_list.KeyList;
import ch.bailu.aat_lib.lib.filter_list.ListEntry;
import ch.bailu.aat_lib.gpx.attributes.Keys;


public final class MapFeaturesListEntry extends ListEntry {
    private final boolean isSummary;
    private final String name, key, value, summarySearchKey, html;

    private final KeyList keys;
    private final int id;

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
        html = parser.addHtml(new StringBuilder()).toString();

        summarySearchKey = parser.getSummarySearchKey();
        keys = new KeyList(summarySearchKey);

        if (!isSummary())
            keys.addKeys(html);

    }

    @Override
    public boolean isSelected() {
        return false;
    }

    @Override
    public boolean isSummary() {
        return isSummary;
    }

    @Override
    public KeyList getKeys() {
        return keys;
    }

    @Override
    public String getSummaryKey() {
        return summarySearchKey;
    }

    public int getID() {
        return id;
    }

    @Override
    public void setSelected(boolean selected) {}

    public int length() {
        return name.length() +
                key.length() +
                html.length() +
                keys.length();
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
        if (!isSummary) {
            list.add("["+ key +"~\"" + value + "\",i];");
        }
        return list;
    }

    public int getOsmKey() {
        return Keys.toIndex(key);
    }

    public String getOsmValue() {
        return value;
    }

    public String getHtml() {
        return html;
    }
}
