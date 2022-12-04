package ch.bailu.aat_gtk.view.toplevel

import ch.bailu.aat_gtk.view.description.NumberView
import ch.bailu.aat_lib.description.ContentDescription
import ch.bailu.aat_lib.dispatcher.DispatcherInterface
import ch.bailu.gtk.gtk.FlowBox
import ch.bailu.gtk.gtk.ScrolledWindow

class CockpitView {

    private val flow = FlowBox()
    val scrolledWindow = ScrolledWindow().apply {
        child = flow
    }

    private val descriptions = ArrayList<ContentDescription>()
    private val views = ArrayList<NumberView>()

    fun add(di: DispatcherInterface, cd: ContentDescription, vararg iid: Int) : NumberView {
        val v = NumberView(cd)

        flow.insert(v.box, -1)
        di.addTarget(v, *iid)
        descriptions.add(cd)
        views.add(v)

       return v
    }
}
