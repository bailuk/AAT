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

                if (model.type != Type.SEPARATOR) {
                    val old = gtk.label
                    gtk.label = Str(model.label)
                    old.destroy()
                }

                if (model.type == Type.RADIO && gtk is RadioMenuItem) {
                    gtk.active = GTK.IS(model.selected)
                }
            }
        }

        model.forEach {
            when (it.type) {
                Type.SEPARATOR -> {
                    add(SeparatorMenuItem(), it)
                }

                Type.RADIO -> {
                    val item = RadioMenuItem(groups[it.group])
                    groups[it.group] = item.group
                    item.onActivate { it.onSelected(it) }
                    add(item, it)
                }

                Type.LABEL -> {
                    val item = MenuItem()
                    item.onActivate { it.onSelected(it) }
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

