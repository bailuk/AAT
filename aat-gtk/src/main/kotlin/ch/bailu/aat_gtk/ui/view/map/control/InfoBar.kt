package ch.bailu.aat_gtk.ui.view.map.control

import ch.bailu.aat_gtk.ui.util.IconMap
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Button
import ch.bailu.gtk.gtk.Orientation

class InfoBar {

    val box = Box(Orientation.VERTICAL, 2)
    private val plus: Button = Button()
    private val minus: Button = Button()
    private val frame: Button = Button()

    private var boundingCycle = 0

    init {


        plus.child = IconMap.getImage("zoom-in-symbolic", BarControl.ICON_SIZE)
        box.append(plus)

        minus.child = IconMap.getImage("zoom-out-symbolic", BarControl.ICON_SIZE)
        box.append(minus)


        frame.child = IconMap.getImage("zoom-fit-best-symbolic", BarControl.ICON_SIZE)
        box.append(frame)
    }
}