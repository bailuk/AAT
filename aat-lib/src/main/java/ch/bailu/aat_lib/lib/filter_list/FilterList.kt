package ch.bailu.aat_lib.lib.filter_list

class FilterList : AbsFilterList<AbsListItem>() {

    override fun showElement(filterList: AbsListItem, keyList: KeyList): Boolean {
        return if (keyList.isEmpty) {
            filterList.isSelected() || filterList.isSummary()
        } else {
            filterList.isSelected() || filterList.getKeys().fits(keyList)
        }
    }
}
