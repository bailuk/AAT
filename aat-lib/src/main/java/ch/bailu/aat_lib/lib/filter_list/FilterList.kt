package ch.bailu.aat_lib.lib.filter_list;

public class FilterList extends AbsFilterList<AbsListItem> {
    @Override
    public boolean showElement(AbsListItem listEntry, KeyList keyList) {
        if (keyList.isEmpty()) {
            return listEntry.isSelected() || listEntry.isSummary();
        } else {
            return listEntry.isSelected() || listEntry.getKeys().fits(keyList);
        }
    }
}
