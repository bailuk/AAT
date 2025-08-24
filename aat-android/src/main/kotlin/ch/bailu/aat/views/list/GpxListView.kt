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

class GpxListView(c: Context, private val data: Array<ContentDescription>) : ListView(c) {
    private var observers = ArrayList<DataSetObserver>()
    private var theme = AppTheme.trackList

    init {
        theme.list(this)
    }

    fun setIterator(acontext: ActivityContext, iterator: Iterator) {
        iterator.setOnCursorChangedListener { observers.forEach {
            it.onChanged() }
        }
        adapter = IteratorAdapter(acontext, iterator)
    }

    fun themify(t: UiTheme) {
        theme = t
    }

    inner class IteratorAdapter(
        private val acontext: ActivityContext,
        private val iterator: Iterator
    ) : ListAdapter {
        override fun getCount(): Int {
            return iterator.getCount()
        }

        override fun getItem(position: Int): View? {
            return null
        }

        override fun getItemId(position: Int): Long {
            iterator.moveToPosition(position)
            return iterator.getID()
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
                entry.onContentUpdated(iterator.getInfoID(), iterator.getInfo())
                entry.themify(theme)
            }
            return entry
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
