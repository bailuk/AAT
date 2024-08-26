package ch.bailu.aat_gtk.view

import ch.bailu.aat_gtk.lib.extensions.margin
import ch.bailu.aat_gtk.view.description.DescriptionLabelTextView
import ch.bailu.aat_lib.description.ContentDescription
import ch.bailu.aat_lib.dispatcher.DispatcherInterface
import ch.bailu.aat_lib.dispatcher.usage.UsageTrackerInterface
import ch.bailu.gtk.gtk.*
import ch.bailu.gtk.type.Str

abstract class VerticalScrollView {
    companion object {
        const val MARGIN = 10
    }

    val scrolled = ScrolledWindow()
    private val container = Box(Orientation.VERTICAL,5)

    init {
        scrolled.child = container
    }

    fun add(child: Widget) {
        child.margin(MARGIN)
        container.append(child)
    }

    fun add(text: String) {
        val label = Label(Str.NULL)
        label.setMarkup("<b>$text</b>")
        add(label)
    }

    fun add(di: DispatcherInterface, desc: ContentDescription, usageTracker: UsageTrackerInterface) {
        val view = DescriptionLabelTextView(desc)
        add(view.layout)
        di.addTarget(ch.bailu.aat_lib.dispatcher.filter.TargetFilter(view, usageTracker))
    }

    fun addAllContent(di: DispatcherInterface, descs: Array<ContentDescription>,
                      usageTracker: UsageTrackerInterface) {
        descs.forEach { add(di, it, usageTracker) }
    }
}
