package ch.bailu.aat_gtk.view.toplevel

import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_lib.preferences.SolidString
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.gtk.adw.Clamp
import ch.bailu.gtk.adw.HeaderBar
import ch.bailu.gtk.adw.ViewStack
import ch.bailu.gtk.adw.ViewSwitcherBar
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Orientation
import ch.bailu.gtk.gtk.Widget
import ch.bailu.gtk.type.Str

class StackView(headerBar: HeaderBar) {
    companion object {
        private const val maximumWidth = Layout.stackWidth
        private const val solidKey = "StackPage"
    }

    private val viewStack = ViewStack().apply {
        vexpand = true
    }

    private val viewStackSwitcher = ViewSwitcherBar().apply {
        stack = viewStack
        reveal = true
    }

    val stackPage = Box(Orientation.VERTICAL, 0).apply {
            append(headerBar)
            append(viewStackSwitcher)
            append(Clamp().apply {
                maximumSize = maximumWidth
                child = viewStack
            })
    }

    fun addView(widget: Widget, pageId: Str, label: String) {
        widget.hexpand = false
        viewStack.addTitledWithIcon(widget, pageId, Str(label), pageId)
    }

    fun restore(storageInterface: StorageInterface) {
        showPage(Str(SolidString(storageInterface, solidKey).valueAsString))
    }

    fun save(storageInterface: StorageInterface) {
        SolidString(storageInterface, solidKey).setValue(viewStack.visibleChildName.toString())
    }

    fun showPage(pageId: Str) {
        viewStack.visibleChildName = pageId
    }


}
