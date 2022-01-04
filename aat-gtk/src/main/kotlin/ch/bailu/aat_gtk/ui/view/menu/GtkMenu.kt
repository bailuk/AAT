package ch.bailu.aat_gtk.ui.view.menu

import ch.bailu.aat_gtk.ui.view.menu.model.Item
import ch.bailu.aat_gtk.ui.view.menu.model.Type
import ch.bailu.gtk.gio.Menu
import ch.bailu.gtk.gio.MenuItem
import ch.bailu.gtk.helper.ActionHelper
import ch.bailu.gtk.type.Str
import java.util.*
import ch.bailu.aat_gtk.ui.view.menu.model.Menu as MenuModel

class GtkMenu(actionHelper: ActionHelper, model: MenuModel){
    val menu = Menu()

    init {

        var section = Menu()
        var id = ""
        var index = 0
        var items = ArrayList<Item>()

        model.forEach { it ->

            when (it.type) {
                Type.SEPARATOR -> {
                    menu.appendSection(null, section)
                    section = Menu()
                    index = 0
                }

                Type.RADIO -> {

                    if (index == 0) {
                        items = ArrayList<Item>()
                        id = UUID.randomUUID().toString()

                        actionHelper.add(id, index) {
                            val index = it?.int32 ?: 0

                            if (index < items.size)
                                items[index].onSelected(items[index])
                        }
                    }
                    items.add(it)
                    val item = MenuItem(Str(it.label), Str("app.${id}(${index})"))
                    section.appendItem(item)

                    index++
                }

                Type.LABEL -> {
                    id = UUID.randomUUID().toString()
                    index = 0

                    val item = MenuItem(Str(it.label), Str("app.${id}"))
                    section.appendItem(item)

                    val i = it
                    actionHelper.add(id) {i.onSelected(i)}
                }
            }
        }
        menu.appendSection(null, section)

    }
}
