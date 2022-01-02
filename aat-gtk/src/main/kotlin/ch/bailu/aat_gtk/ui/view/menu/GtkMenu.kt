package ch.bailu.aat_gtk.ui.view.menu

import ch.bailu.aat_gtk.ui.view.menu.model.Type
import ch.bailu.gtk.GTK
import ch.bailu.gtk.gio.ActionMap
import ch.bailu.gtk.gio.Menu
import ch.bailu.gtk.gio.MenuItem
import ch.bailu.gtk.helper.ActionHelper
import ch.bailu.gtk.type.Str
import ch.bailu.aat_gtk.ui.view.menu.model.Menu as MenuModel

class GtkMenu(actionMap: ActionMap, model: MenuModel){
    val menu = Menu()

    init {
        val actions = ActionHelper(actionMap, "app")

        var index = 0
        model.forEach {
            when (it.type) {
                Type.SEPARATOR -> {
                    //
                }

                Type.RADIO -> {
                    val item = MenuItem(Str(it.label), Str("app.${index}"))
                    menu.appendItem(item)

                    val i = it
                    actions.addBoolean("${index}", GTK.TRUE) {i.onSelected(i)}
                }

                Type.LABEL -> {
                    val item = MenuItem(Str(it.label), Str("app.${index}"))
                    menu.appendItem(item)

                    val i = it
                    actions.add("${index}") {i.onSelected(i)}
                }
            }
            index++
        }
    }
}

