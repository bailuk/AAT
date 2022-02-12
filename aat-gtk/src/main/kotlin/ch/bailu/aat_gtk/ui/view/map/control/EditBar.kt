package ch.bailu.aat_gtk.ui.view.map.control

import ch.bailu.gtk.gtk.Orientation

class EditBar {
    val bar = Bar(Orientation.VERTICAL)

    private val minus = bar.add("zoom-out-symbolic")
    private val frame = bar.add("zoom-fit-best-symbolic")
}