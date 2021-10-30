package ch.bailu.aat_gtk.ui.view.menu

import ch.bailu.aat_gtk.ui.view.menu.model.*
import ch.bailu.aat_gtk.util.IndexedMap
import ch.bailu.gtk.GTK
import ch.bailu.gtk.glib.SList
import ch.bailu.gtk.gtk.*
import ch.bailu.gtk.type.Str
import ch.bailu.aat_gtk.ui.view.menu.model.Menu as MenuModel

class GtkMenu(model: MenuModel){
    val menu = Menu()

    private val items = IndexedMap<MenuItem, Item>()
    private val groups = HashMap<Group, SList>()

    init {
        menu.onShow {
            items.forEach { gtk, model ->
                if (model is LabelItem) {
                    val old = gtk.label
                    gtk.label = Str(model.label)
                    old.destroy()
                }

                if (model is CheckItem && gtk is CheckMenuItem) {
                    gtk.active = GTK.IS(model.selected)
                }

                if (model is RadioItem && gtk is RadioMenuItem) {
                    gtk.active = GTK.IS(model.selected)
                }
            }
        }

        model.forEach {
            when (it) {
                is SeparatorItem -> {
                    add(SeparatorMenuItem(), it)
                }

                is CheckItem -> {
                    val item = CheckMenuItem()
                    item.onActivate {
                        it.selected = GTK.IS(item.active)
                        it.onSelect(it)
                    }
                    add(item, it)
                }

                is RadioItem -> {
                    val item = RadioMenuItem(groups[it.group])
                    groups[it.group] = item.group
                    item.onActivate { it.onSelect(it) }
                    add(item, it)
                }

                is LabelItem -> {
                    val item = MenuItem()
                    item.onActivate { it.onSelect(it) }
                    add(item, it)
                }
            }
        }
        menu.showAll()
    }

    private fun add(key: MenuItem, value: Item) {
        menu.add(key)
        items.put(key, value)
    }
}

