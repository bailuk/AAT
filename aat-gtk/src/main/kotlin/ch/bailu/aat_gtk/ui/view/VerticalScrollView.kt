package ch.bailu.aat_gtk.ui.view

import ch.bailu.aat_gtk.ui.view.description.DescriptionLabelTextView
import ch.bailu.aat_lib.description.ContentDescription
import ch.bailu.aat_lib.dispatcher.DispatcherInterface
import ch.bailu.gtk.gtk.*
import ch.bailu.gtk.type.Str

abstract class VerticalScrollView() {
    val scrolled = ScrolledWindow()
    private val container = Box(Orientation.VERTICAL,5)

    init {
        scrolled.child = container
    }

    fun add(child: Widget) {
        container.append(child)
    }

    fun add(text: String) {
        val label = Label(Str.NULL)
        label.setMarkup(Str("<b>${text}</b>"))
        add(label)
    }

    fun add(di: DispatcherInterface, desc: ContentDescription, vararg iid: Int) {
        val view = DescriptionLabelTextView(desc)
        add(view.layout)
        iid.forEach { di.addTarget(view, it) }
    }

    fun addAllContent(di: DispatcherInterface, descs: Array<ContentDescription>, vararg iid: Int) {
        descs.forEach { add(di, it, *iid) }
    }
}
