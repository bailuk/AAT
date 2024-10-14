package ch.bailu.aat.services.cache.osm_features

import ch.bailu.aat_lib.gpx.attributes.Keys
import ch.bailu.aat_lib.lib.filter_list.AbsListItem
import ch.bailu.aat_lib.lib.filter_list.KeyList

class MapFeaturesListItem(parser: MapFeaturesParser) : AbsListItem() {
    private val isSummary: Boolean
    private val name: String
    private val key: String

    val osmValue: String
    private val summarySearchKey: String
    val html: String
    private val keys: KeyList
    private val id: Int

    init {
        val k = parser.key
        isSummary = k.isEmpty()

        name = parser.name
        key = if (isSummary) {
            parser.summaryKey
        } else {
            k
        }

        osmValue = parser.value
        id = parser.id
        html = parser.addHtml(StringBuilder()).toString()
        summarySearchKey = parser.summarySearchKey
        keys = KeyList(summarySearchKey)
        if (!isSummary()) keys.addKeys(html)
    }

    override fun isSelected(): Boolean {
        return false
    }

    override fun isSummary(): Boolean {
        return isSummary
    }

    override fun getKeys(): KeyList {
        return keys
    }

    override fun getSummaryKey(): String {
        return summarySearchKey
    }

    override fun getID(): Int {
        return id
    }

    override fun setSelected(selected: Boolean) {}
    fun length(): Int {
        return name.length +
                key.length +
                html.length +
                keys.length()
    }

    val defaultQuery: String
        get() = if (key.isNotEmpty()) {
            if (osmValue.isNotEmpty()) {
                "[$key=\"$osmValue\"];"
            } else "[$key];"
        } else ""
    val variants: ArrayList<String>
        get() {
            val list = ArrayList<String>(10)
            list.add(defaultQuery)
            if (!isSummary) {
                list.add("[$key~\"$osmValue\",i];")
            }
            return list
        }
    val osmKey: Int
        get() = Keys.toIndex(key)
}
