package ch.bailu.aat.views.list

import android.content.Context
import android.database.DataSetObserver
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.ListView
import ch.bailu.aat.activities.ActivityContext
import ch.bailu.aat.util.ui.theme.AppTheme
import ch.bailu.aat.util.ui.theme.UiTheme
import ch.bailu.aat_lib.description.ContentDescription
import ch.bailu.aat_lib.service.directory.Iterator
import ch.bailu.aat_lib.service.directory.Iterator.OnCursorChangedListener

class GpxListView(c: Context, private val data: Array<ContentDescription>) : ListView(c),
    OnCursorChangedListener {
    private var observers = ArrayList<DataSetObserver>()
    private var theme = AppTheme.trackList

    init {
        theme.list(this)
    }

    fun setIterator(acontext: ActivityContext, iterator: Iterator) {
        iterator.setOnCursorChangedListener(this)
        adapter = IteratorAdapter(acontext, iterator)
    }

    override fun onCursorChanged() {
        observers.forEach { it.onChanged() }
    }

    fun themify(t: UiTheme) {
        theme = t
    }

    inner class IteratorAdapter(
        private val acontext: ActivityContext,
        private val iterator: Iterator
    ) : ListAdapter {
        override fun getCount(): Int {
            return iterator.count
        }

        override fun getItem(position: Int): View? {
            return null
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getItemViewType(position: Int): Int {
            return 0
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var entry = convertView

            if (entry == null) {
                entry = GpxListEntryView(acontext, data, theme)
            }

            if (entry is GpxListEntryView) {
                iterator.moveToPosition(position)
                entry.onContentUpdated(iterator.infoID, iterator.info)
                entry.themify(theme)
            }
            return entry
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
            observers.add(o)
        }

        override fun unregisterDataSetObserver(o: DataSetObserver) {
            observers.remove(o)
        }

        override fun areAllItemsEnabled(): Boolean {
            return true
        }

        override fun isEnabled(position: Int): Boolean {
            return true
        }
    }
}
