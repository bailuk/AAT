package ch.bailu.aat_gtk.view.toplevel

import ch.bailu.aat_lib.preferences.SolidString
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.gtk.adw.ViewStack
import ch.bailu.gtk.adw.ViewSwitcherBar
import ch.bailu.gtk.gtk.Widget
import ch.bailu.gtk.type.Str

class StackView(private val key: String) {

    val viewStack = ViewStack().apply {
        vexpand = true
    }

    val viewSwitcherBar = ViewSwitcherBar().apply {
        stack = viewStack
        reveal = true
    }

    fun addView(widget: Widget, pageId: Str, label: String) {
        widget.hexpand = false
        viewStack.addTitledWithIcon(widget, pageId, Str(label), pageId)
    }

    fun restore(storageInterface: StorageInterface) {
        showPage(Str(SolidString(storageInterface, key).getValueAsString()))
    }

    fun save(storageInterface: StorageInterface) {
        SolidString(storageInterface, key).setValue(viewStack.visibleChildName.toString())
    }

    fun showPage(pageId: Str) {
        viewStack.visibleChildName = pageId
    }
}
