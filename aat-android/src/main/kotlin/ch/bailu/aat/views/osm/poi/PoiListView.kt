package ch.bailu.aat.views.osm.poi

import android.content.Context
import android.database.DataSetObserver
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListAdapter
import android.widget.ListView
import ch.bailu.aat.util.ui.theme.AppTheme
import ch.bailu.aat.util.ui.theme.UiTheme
import ch.bailu.aat.views.osm.features.OnSelected
import ch.bailu.aat_lib.lib.filter_list.FilterList
import ch.bailu.aat_lib.search.poi.PoiListItem

class PoiListView(context: Context, private val list: FilterList, private val theme: UiTheme) :
    ListView(context) {
    private var observers = ArrayList<DataSetObserver>()
    private var onSelected = OnSelected.NULL

    init {
        val listAdapter = Adapter()
        AppTheme.search.list(this)
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

        override fun getView(index: Int, view: View?, parent: ViewGroup?): View {
            val entryView: PoiListEntryView = if (view is PoiListEntryView) {
                view
            } else {
                PoiListEntryView(this@PoiListView.context, onSelected, theme)
            }
            entryView.set((list.getFromVisible(index) as PoiListItem))
            return entryView
        }

        override fun registerDataSetObserver(observer: DataSetObserver) {
            observers.add(observer)
        }

        override fun unregisterDataSetObserver(observer: DataSetObserver) {
            observers.remove(observer)
        }

        override fun onItemClick(arg0: AdapterView<*>?, view: View, index: Int, arg3: Long) {
            onSelected.onSelected(list.getFromVisible(index), OnSelected.Action.Edit, "")
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
}
