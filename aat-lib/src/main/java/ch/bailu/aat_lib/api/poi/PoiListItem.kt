package ch.bailu.aat_lib.api.poi

import ch.bailu.aat_lib.lib.filter_list.AbsListItem
import ch.bailu.aat_lib.lib.filter_list.KeyList
import org.mapsforge.poi.storage.PoiCategory

class PoiListItem : AbsListItem {
    val category: PoiCategory

    private val isSummary: Boolean
    private var isSelected = false

    private val keys: KeyList
    private val summaryKey: String


    constructor(category: PoiCategory) {
        isSummary = true
        this.category = category

        keys = KeyList(category.title)
        summaryKey = "_" + keys.getKey(0)
        keys.addKeys(summaryKey)
    }

    constructor(category: PoiCategory, summary: PoiListItem) {
        isSummary = false

        this.category = category

        summaryKey = summary.getSummaryKey()
        keys = KeyList(category.title)
        keys.addKeys(summaryKey)
    }

    override fun isSelected(): Boolean {
        return isSelected
    }

    override fun isSummary(): Boolean {
        return isSummary
    }

    override fun getKeys(): KeyList {
        return keys
    }

    override fun getSummaryKey(): String {
        return summaryKey
    }

    val title: String
        get() = category.title

    override fun getID(): Int {
        return category.id
    }

    override fun setSelected(selected: Boolean) {
        if (!isSummary()) isSelected = selected
    }
}
