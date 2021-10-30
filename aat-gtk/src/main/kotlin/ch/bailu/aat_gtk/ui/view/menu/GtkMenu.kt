package ch.bailu.aat_gtk.ui.view.menu

import ch.bailu.aat_gtk.ui.view.menu.model.*
import ch.bailu.gtk.GTK
import ch.bailu.gtk.glib.SList
import ch.bailu.gtk.gtk.*
import ch.bailu.gtk.type.Str
import ch.bailu.aat_gtk.ui.view.menu.model.Menu as MenuModel

class GtkMenu(private val model: MenuModel){
    val menu = Menu()

    private val groups = HashMap<Group, SList>()


    init {
        model.forEach {
            when (it) {
                is SeparatorItem -> {
                    menu.add(SeparatorMenuItem())
                }

                is CheckItem -> {
                    val item = CheckMenuItem()
                    item.label = Str(it.label)
                    item.active = GTK.IS(it.selected)
                    item.onActivate { it.onSelect(GTK.IS(item.active)) }
                    menu.add(item)

                }

                is RadioItem -> {
                    val group = getGroup(it.group)
                    val item = RadioMenuItem(group)
                    item.label = Str(it.label)
                    item.active = GTK.IS(it.selected)
                    item.onActivate { it.onSelect(true) }
                    menu.add(item)
                }

                is LabelItem -> {
                    val item = MenuItem()
                    item.label = Str(it.label)
                    item.onActivate { it.onSelect(true) }
                    menu.add(item)
                }
            }
        }
        menu.showAll()
    }

    private fun getGroup(group: Group): SList {
        var slist = groups[group]

        return if (slist == null) {
            slist = SList(0)
            groups[group] = slist
            slist
        } else {
            slist
        }
    }
}

