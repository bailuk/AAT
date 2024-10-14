package ch.bailu.aat_lib.lib.filter_list

abstract class AbsListItem {
    abstract fun isSelected(): Boolean
    abstract fun setSelected(selected: Boolean)
    abstract fun isSummary(): Boolean
    abstract fun getKeys(): KeyList
    abstract fun getSummaryKey(): String
    abstract fun getID(): Int
}
