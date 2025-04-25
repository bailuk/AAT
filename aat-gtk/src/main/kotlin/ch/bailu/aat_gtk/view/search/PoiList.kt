package ch.bailu.aat_gtk.view.search

import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_lib.lib.filter_list.FilterList
import ch.bailu.aat_lib.lib.filter_list.FilterListUtil
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.preferences.SolidPoiDatabase
import ch.bailu.aat_lib.search.poi.PoiListItem
import ch.bailu.foc.Foc
import ch.bailu.gtk.gtk.ListItem
import ch.bailu.gtk.gtk.ListView
import ch.bailu.gtk.gtk.ScrollablePolicy
import ch.bailu.gtk.gtk.ScrolledWindow
import ch.bailu.gtk.gtk.SignalListItemFactory
import ch.bailu.gtk.lib.bridge.ListIndex
import org.mapsforge.poi.storage.PoiCategory

class PoiList(
    private val sdatabase: SolidPoiDatabase,
    private val selected: Foc,
    private val onSelected: (model: PoiListItem) -> Unit
) {

    private val listIndex = ListIndex()
    private val filterList = FilterList()
    private val items = HashMap<ListItem, PoiListItemView>()

    private val list = ListView(listIndex.inSelectionModel(), SignalListItemFactory().apply {
        onSetup {
            val item = ListItem(it.cast())
            items[item] = PoiListItemView(item)
        }

        onBind {
            val item = ListItem(it.cast())

            val view = items[it]

            if (view is PoiListItemView) {
                val model = filterList.getFromVisible(item.position)
                if (model is PoiListItem) {
                    view.set(model)
                }
            }
        }

        onTeardown {
            val view = items.remove(it)
            if (view is PoiListItemView) {
                view.onTeardown()
            }
        }
    }).apply {
        onActivate {
            val model = filterList.getFromVisible(it)
            if (model is PoiListItem) {
                onSelected(model)
            }
        }
    }

    val scrolled = ScrolledWindow().apply {
        child = list
        hexpand = true
        vexpand = true
    }

    init {
        readList()
    }

    fun readList() {
        FilterListUtil.readList(filterList, GtkAppContext, sdatabase.getValueAsString(), selected)
        filterList.filterAll()
        listIndex.size = filterList.sizeVisible()
    }

    fun updateList(text: String) {
        filterList.filter(text)
        listIndex.size = filterList.sizeVisible()
    }

    fun getSelectedCategories(): ArrayList<PoiCategory> {
        val result = ArrayList<PoiCategory>(10)
        for (i in 0 until filterList.sizeAll()) {
            val e = filterList.getFromAll(i) as PoiListItem
            if (e.isSelected()) {
                result.add(e.category)
            }
        }
        return result
    }

    fun writeSelected() {
        try {
            FilterListUtil.writeSelected(filterList, selected)
        } catch (e: Exception) {
            AppLog.e(this, e)
        }
    }
}
