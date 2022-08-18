package ch.bailu.aat_lib.search.poi;

import org.mapsforge.poi.storage.PoiCategory;

import ch.bailu.aat_lib.lib.filter_list.KeyList;
import ch.bailu.aat_lib.lib.filter_list.ListEntry;

public class PoiListItem extends ListEntry {
    private final PoiCategory self;

    private final boolean isSummary;
    private boolean isSelected = false;

    private final KeyList keys;
    private final String summaryKey;


    public PoiListItem(PoiCategory c) {
        isSummary = true;
        self = c;

        keys = new KeyList(c.getTitle());
        summaryKey = "_" + keys.getKey(0);
        keys.addKeys(summaryKey);
    }

    public PoiListItem(PoiCategory c, PoiListItem s) {
        isSummary = false;

        self = c;

        summaryKey = s.getSummaryKey();
        keys = new KeyList(c.getTitle());
        keys.addKeys(summaryKey);
    }

    @Override
    public boolean isSelected() {
        return isSelected;
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
        return summaryKey;
    }


    public String getTitle() {
        return self.getTitle();
    }

    public int getID() {
        return self.getID();
    }

    @Override
    public void select() {
        if (!isSummary()) isSelected = !isSelected;
    }

    public PoiCategory getCategory() {
        return self;
    }
}
