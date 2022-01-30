package ch.bailu.aat_gtk.ui.view.description

import ch.bailu.aat_lib.description.ContentDescription
import ch.bailu.aat_lib.dispatcher.DispatcherInterface
import ch.bailu.gtk.gtk.Fixed

class CockpitView {
    val fixed = Fixed()


    fun add(di: DispatcherInterface, cd: ContentDescription, vararg iid: Int) : NumberView {
        var v = NumberView(cd)

        v.box.setSizeRequest(50,50)
        fixed.put(v.box, 0.0,0.0)

        di.addTarget(v, *iid)
        return v
    }
}