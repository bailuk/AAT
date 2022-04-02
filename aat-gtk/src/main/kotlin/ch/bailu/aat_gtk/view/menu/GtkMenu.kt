package ch.bailu.aat_gtk.view.menu

import ch.bailu.aat_gtk.view.menu.model.*
import ch.bailu.aat_gtk.view.util.truncate
import ch.bailu.gtk.gio.Menu
import ch.bailu.gtk.gio.MenuItem
import ch.bailu.gtk.helper.ActionHelper
import ch.bailu.gtk.type.Str
import ch.bailu.aat_gtk.view.menu.model.Menu as MenuModel

class GtkMenu(actionHelper: ActionHelper, model: MenuModel){
    val menu = Menu().apply {
        appendSection(strOrNull(model.title), createMenu(actionHelper, model))
    }



    private fun createMenu(actionHelper: ActionHelper, model: MenuModel) : Menu {
        val result = Menu()

        var index = 0
        var items = ArrayList<RadioItem>()
        var group = Group()


        model.forEach { it ->

            when (it) {
                is SubmenuItem -> {
                    result.appendSubmenu(Str(it.menu.title), createMenu(actionHelper, it.menu))
                }

                is SeparatorItem -> {
                    result.appendSection(strOrNull(it.menu.title), createMenu(actionHelper, it.menu))
                }

                is RadioItem -> {
                    if (group != it.group) {
                        index = 0
                        group = it.group
                        items = ArrayList<RadioItem>()

                        actionHelper.add(group.id, index) {
                            val idx = it?.int32 ?: 0

                            if (idx < items.size)
                                items[idx].onSelected(items[idx])
                        }
                    }
                    items.add(it)
                    val item = MenuItem(Str(it.label), Str("app.${group.id}(${index})"))
                    result.appendItem(item)

                    index++
                }

                is LabelItem -> {
                    result.appendItem(toGtkItem(it.label, it.id))
                    val i = it
                    actionHelper.add(it.id) { i.onSelected(i) }
                }

                is CheckItem -> {
                    result.appendItem(toGtkItem(it.label, it.id))
                    val i = it
                    actionHelper.add(it.id, it.selected) { i.onSelected(i) }
                }
            }
        }
        return result
    }

    private fun toGtkItem(label : String, id: String) : MenuItem {
        return MenuItem(Str(label.truncate()), Str("app.${id}"))
    }

    private fun strOrNull(title: String): Str? {
        return if (title.isNotEmpty()) {
            Str(title)
        } else {
            null
        }
    }
}
