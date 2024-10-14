package ch.bailu.aat_lib.lib.filter_list

class FilterList : AbsFilterList<AbsListItem>() {

    override fun showElement(listEntry: AbsListItem, keyList: KeyList): Boolean {
        return if (keyList.isEmpty) {
            listEntry.isSelected() || listEntry.isSummary()
        } else {
            listEntry.isSelected() || listEntry.getKeys().fits(keyList)
        }
    }
}
