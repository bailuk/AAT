package ch.bailu.aat_gtk.ui.view.map.control

import ch.bailu.gtk.GTK
import ch.bailu.gtk.gtk.Align
import ch.bailu.gtk.gtk.Box

class BarControl  {
    companion object {
        const val ICON_SIZE = 24
        const val TOP = 0
        const val LEFT = 1
        const val BOTTOM = 2
        const val RIGHT = 3
    }


    private val controlBars = HashMap<Int, Box>()

    open fun isVisible(pos: Int): Boolean {
        return GTK.IS(controlBars[pos]?.visible ?: GTK.FALSE)
    }

    fun add(bar: Box, pos: Int): Box {
        controlBars[pos] = bar

        if (pos == TOP) {
            bar.halign = Align.FILL
            bar.valign = Align.START
        } else if (pos == BOTTOM) {
            bar.halign = Align.FILL
            bar.valign = Align.END
        } else if (pos == LEFT) {
            bar.valign = Align.FILL
            bar.halign = Align.END
        } else if (pos == RIGHT) {
            bar.valign = Align.FILL
            bar.halign = Align.START
        }
        return bar
    }


    fun hide() {
        controlBars.forEach { it.value.hide() }
    }

    fun show(p: Int) {
        controlBars.forEach {
            if (it.key == p) {
                it.value.visible = GTK.TRUE
            } else {
                it.value.visible = GTK.FALSE
            }
        }
    }
}