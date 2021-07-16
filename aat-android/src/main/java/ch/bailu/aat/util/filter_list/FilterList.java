package ch.bailu.aat.util.filter_list;

public class FilterList extends AbsFilterList<ListEntry> {
    @Override
    public boolean showElement(ListEntry listEntry, KeyList keyList) {
        if (keyList.isEmpty()) {
            return listEntry.isSelected() || listEntry.isSummary();
        } else {
            return listEntry.isSelected() || listEntry.getKeys().fits(keyList);
        }
    }
}
