package ch.bailu.aat.views.osm.features

import android.database.DataSetObserver
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListAdapter
import android.widget.ListView
import ch.bailu.aat.services.ServiceContext
import ch.bailu.aat.services.cache.osm_features.MapFeaturesListItem
import ch.bailu.aat.util.ui.theme.AppTheme
import ch.bailu.aat_lib.lib.filter_list.AbsFilterList
import ch.bailu.aat_lib.lib.filter_list.AbsListItem

class MapFeaturesListView(private val scontext: ServiceContext, private val list: AbsFilterList<AbsListItem>) : ListView(
    scontext.getContext()
) {

    private val observers = ArrayList<DataSetObserver>()
    private var onSelected = OnSelected.NULL

    init {
        val listAdapter = Adapter()
        theme.list(this)
        adapter = listAdapter
        onItemClickListener = listAdapter
    }

    fun onChanged() {
        observers.forEach { it.onChanged() }
    }

    fun setOnTextSelected(s: OnSelected) {
        onSelected = s
    }

    private inner class Adapter : ListAdapter, OnItemClickListener {
        override fun getCount(): Int {
            return list.sizeVisible()
        }

        override fun getView(index: Int, v: View?, p: ViewGroup): View {
            return toEntryView(v).apply {
                set((list.getFromVisible(index) as MapFeaturesListItem))
            }
        }

        private fun toEntryView(view: View?): MapFeaturesEntryView {
            return if (view is MapFeaturesEntryView) {
                view
            } else {
                MapFeaturesEntryView(scontext, onSelected, theme)
            }
        }

        override fun registerDataSetObserver(observer: DataSetObserver) {
            observers.add(observer)
        }

        override fun unregisterDataSetObserver(observer: DataSetObserver) {
            observers.remove(observer)
        }

        override fun onItemClick(arg0: AdapterView<*>?, view: View, index: Int, arg3: Long) {
            val listEntry = list.getFromVisible(index)
            if (listEntry is MapFeaturesListItem) {
                if (listEntry.isSummary()) {
                    onSelected.onSelected(listEntry, OnSelected.Action.Filter, listEntry.getSummaryKey())
                } else onSelected.onSelected(listEntry, OnSelected.Action.Edit, listEntry.defaultQuery)
            }
        }

        override fun getItem(position: Int): Any {
            return list.getFromVisible(position)
        }

        override fun getItemId(position: Int): Long {
            return list.getFromVisible(position).getID().toLong()
        }

        override fun getItemViewType(position: Int): Int {
            return 0
        }

        override fun getViewTypeCount(): Int {
            return 1
        }

        override fun hasStableIds(): Boolean {
            return true
        }

        override fun isEmpty(): Boolean {
            return count == 0
        }

        override fun areAllItemsEnabled(): Boolean {
            return true
        }

        override fun isEnabled(index: Int): Boolean {
            return true
        }
    }

    companion object {
        private val theme = AppTheme.search
    }
}
