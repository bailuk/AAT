package ch.bailu.aat_gtk.adw_view

import ch.bailu.aat_lib.preferences.SolidString
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.gtk.adw.Clamp
import ch.bailu.gtk.adw.ViewStack
import ch.bailu.gtk.adw.ViewSwitcherBar
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Orientation
import ch.bailu.gtk.gtk.Widget
import ch.bailu.gtk.type.Str

class AdwStackPage {
    companion object {
        private const val maximumWidth = 400
        private const val solidKey = "StackPage"
    }

    private val viewStack = ViewStack().apply {
        vexpand = true
    }

    private val viewStackSwitcher = ViewSwitcherBar().apply {
        stack = viewStack
        reveal = true
    }

    val stackPage = Clamp().apply {
        maximumSize = maximumWidth

        child = Box(Orientation.VERTICAL, 0).apply {
            append(viewStack)
            append(viewStackSwitcher)
        }
    }

    fun addView(widget: Widget, iconName: String, label: String) {
        widget.hexpand = false
        viewStack.addTitledWithIcon(widget, iconName, label, iconName)
    }

    fun restore(storageInterface: StorageInterface) {
        val str = Str(SolidString(storageInterface, solidKey).valueAsString)
        viewStack.visibleChildName = str
        str.destroy()
    }

    fun save(storageInterface: StorageInterface) {
        val str = viewStack.visibleChildName
        SolidString(storageInterface, solidKey).setValue(str.toString())
        str.destroy()
    }


}
