package ch.bailu.aat_gtk.view.search

import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_lib.lib.filter_list.FilterList
import ch.bailu.aat_lib.preferences.SolidPoiDatabase
import ch.bailu.aat_lib.search.poi.FilterListUtilPoi
import ch.bailu.foc.Foc
import ch.bailu.gtk.GTK
import ch.bailu.gtk.bridge.ListIndex
import ch.bailu.gtk.gtk.ListView
import ch.bailu.gtk.gtk.ScrolledWindow
import ch.bailu.gtk.gtk.SignalListItemFactory

class PoiList(private val sdatabase: SolidPoiDatabase, private val selected: Foc) {

    private val listIndex = ListIndex()
    private val filterList = FilterList()

    private val list = ListView(listIndex.inSelectionModel(), SignalListItemFactory().apply {
        onSetup {}
        onBind {}
        onTeardown {}
    })

    val scrolled = ScrolledWindow().apply {
        child = list
        hexpand = GTK.TRUE
        vexpand = GTK.TRUE
    }

    init {
        readList()
    }

    private fun readList() {
        FilterListUtilPoi.readList(filterList, GtkAppContext, sdatabase.valueAsString, selected)
        listIndex.size = filterList.sizeVisible()
    }
}
