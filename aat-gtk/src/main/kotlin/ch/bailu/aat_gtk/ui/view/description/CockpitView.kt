package ch.bailu.aat_gtk.ui.view.description

import ch.bailu.aat_lib.description.ContentDescription
import ch.bailu.aat_lib.dispatcher.DispatcherInterface
import ch.bailu.aat_lib.view.cockpit.Layouter
import ch.bailu.gtk.gtk.Fixed

class CockpitView {
    val layout = Fixed()

    private val descriptions = ArrayList<ContentDescription>()
    private val views = ArrayList<NumberView>()


    fun add(di: DispatcherInterface, cd: ContentDescription, vararg iid: Int) : NumberView {
        var v = NumberView(cd)

        layout.put(v.box, 0.0,0.0)

        di.addTarget(v, *iid)
        descriptions.add(cd)
        views.add(v)
        return v
    }

    fun layout() {
        var size = 1000000

        Layouter(descriptions) { index: Int, x: Int, y: Int, x2: Int, y2: Int ->
            size = Math.min(views[index].calculateFontSize((x2-x).toFloat(), (y2-y).toFloat()), size)
            layout.move(views[index].box, x.toDouble(),y.toDouble())
        }.layout(layout.width, layout.height)

        views.forEach {
            it.setFontSize(size)
        }
    }
}
