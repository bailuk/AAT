package ch.bailu.aat.views.list

import android.content.Intent
import android.database.DataSetObserver
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListAdapter
import android.widget.ListView
import ch.bailu.aat.activities.ActivityContext
import ch.bailu.aat.activities.NodeDetailActivity
import ch.bailu.aat.app.ActivitySwitcher.Companion.start
import ch.bailu.aat.util.ui.theme.AppTheme
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.GpxInformationCache
import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.gpx.GpxListArray

class NodeListView(private val dispatcher: ActivityContext) : ListView(
    dispatcher
), OnContentUpdatedInterface, ListAdapter, OnItemClickListener {
    private val observer = SparseArray<DataSetObserver>(5)
    private var array = GpxListArray(GpxList.NULL_ROUTE)
    private val cachedInfo = GpxInformationCache()

    init {
        THEME.list(this)
        adapter = this
        onItemClickListener = this
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        array = GpxListArray(info.getGpxList())
        cachedInfo[iid] = info
        notifyDataSetChanged()
    }

    private fun notifyDataSetChanged() {
        for (i in 0 until observer.size()) {
            observer.valueAt(i).onChanged()
        }
    }

    override fun getCount(): Int {
        return array.size()
    }

    override fun getView(position: Int, recycle: View?, parent: ViewGroup): View {
        var entry = recycle
        if (entry == null) {
            entry = NodeEntryView(dispatcher)
        }

        if (entry is NodeEntryView) {
            entry.update(cachedInfo.infoID, cachedInfo.info, array[position])
        }
        return entry
    }

    override fun getItem(position: Int): Any {
        return array[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onItemClick(arg0: AdapterView<*>?, arg1: View, pos: Int, arg3: Long) {
        val intent = Intent()
        intent.putExtra("I", pos)
        intent.putExtra("ID", cachedInfo.info.getFile().toString())
        start(context, NodeDetailActivity::class.java, intent)
    }

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isEmpty(): Boolean {
        return count == 0
    }

    override fun registerDataSetObserver(o: DataSetObserver) {
        observer.put(o.hashCode(), o)
        notifyDataSetChanged()
    }

    override fun unregisterDataSetObserver(o: DataSetObserver) {
        observer.delete(o.hashCode())
    }

    override fun areAllItemsEnabled(): Boolean {
        return true
    }

    override fun isEnabled(position: Int): Boolean {
        return true
    }

    companion object {
        private val THEME = AppTheme.search
    }
}
