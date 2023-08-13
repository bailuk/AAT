package ch.bailu.aat.views.osm.features

import ch.bailu.aat_lib.lib.filter_list.AbsListItem

fun interface OnSelected {
    enum class Action { Edit, Filter }

    fun onSelected(e: AbsListItem, action: Action, variant: String)

    companion object {

        @JvmField
        val NULL = OnSelected { _: AbsListItem, _: Action, _: String -> }
    }
}
