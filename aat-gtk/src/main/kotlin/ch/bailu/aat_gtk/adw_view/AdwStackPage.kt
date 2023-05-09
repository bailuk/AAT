package ch.bailu.aat_gtk.adw_view

import ch.bailu.gtk.adw.Clamp
import ch.bailu.gtk.adw.ViewStack
import ch.bailu.gtk.adw.ViewSwitcherBar
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Orientation
import ch.bailu.gtk.gtk.Widget

class AdwStackPage {
    companion object {
        const val maximumWidth = 400
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
}
