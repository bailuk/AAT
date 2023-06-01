package ch.bailu.aat_gtk.view.menu

import ch.bailu.aat_lib.resources.Res
import ch.bailu.gtk.gio.MenuItem
import ch.bailu.gtk.glib.Variant
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.lib.handler.action.ActionHandler

object MenuHelper {
    fun setAction(app: Application, action: String, onActivate: ()->Unit) {
        ActionHandler.get(app, toActionName(action)).apply {
            disconnectSignals()
            onActivate(onActivate)
        }
    }

    private fun toActionName(action: String): String {
        val parts = action.split(".")
        return parts[parts.lastIndex]
    }

    fun createCustomItem(customId: String): MenuItem {
        return MenuItem(Res.str().tracker(), "app.$customId").apply {
            setAttribute("custom", null)
            setAttributeValue("custom", Variant.newStringVariant(customId))
        }
    }
}
